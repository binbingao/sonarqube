/*
 * SonarQube
 * Copyright (C) 2009-2017 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.server.platform.db.migration.version.v70;

import java.sql.SQLException;
import org.sonar.api.utils.System2;
import org.sonar.core.util.Uuids;
import org.sonar.db.Database;
import org.sonar.server.platform.db.migration.step.DataChange;
import org.sonar.server.platform.db.migration.step.MassUpdate;

public class PopulateLiveMeasures extends DataChange {

  private final System2 system2;

  public PopulateLiveMeasures(Database db, System2 system2) {
    super(db);
    this.system2 = system2;
  }

  @Override
  protected void execute(Context context) throws SQLException {
    long now = system2.now();
    MassUpdate massUpdate = context.prepareMassUpdate();
    // TODO reentrancy
    massUpdate.select("SELECT p.uuid, p.project_uuid, pm.metric_id, pm.value, pm.text_value, pm.variation_value_1, pm.measure_data, pm.alert_status, pm.alert_text " +
      "FROM project_measures pm " +
      "INNER JOIN projects p on p.uuid = pm.component_uuid " +
      "INNER JOIN snapshots s on s.uuid = pm.analysis_uuid " +
      "WHERE s.islast=?")
      .setBoolean(1, true);

    massUpdate.update("INSERT INTO live_measures "
      + "(uuid, component_uuid, project_uuid, metric_id, value, text_value, variation, measure_data, gate_status, gate_text, created_at, updated_at) "
      + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

    massUpdate.rowPluralName("live measures");
    massUpdate.execute((row, update) -> {
      update.setString(1, Uuids.create());
      update.setString(2, row.getString(1));
      update.setString(3, row.getString(2));
      update.setInt(4, row.getInt(3));
      update.setDouble(5, row.getNullableDouble(4));
      update.setString(6, row.getString(5));
      update.setDouble(7, row.getNullableDouble(6));
      update.setBytes(8, row.getNullableBytes(7));
      update.setString(9, row.getString(8));
      update.setString(10, row.getString(9));
      update.setLong(11, now);
      update.setLong(12, now);
      return true;
    });
  }

}
