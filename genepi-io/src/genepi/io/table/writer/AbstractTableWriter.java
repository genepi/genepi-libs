/*******************************************************************************
 * CONAN: Copy Number Variation Analysis Software for
 * Next Generation Genome-Wide Association Studies
 * 
 * Copyright (C) 2009, 2010 Lukas Forer
 *  
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package genepi.io.table.writer;

public abstract class AbstractTableWriter implements ITableWriter {

	@Override
	public void setInteger(String column, int value) {
		setInteger(getColumnIndex(column), value);

	}

	@Override
	public void setDouble(String column, double value) {
		setDouble(getColumnIndex(column), value);

	}

	@Override
	public void setString(String column, String value) {
		setString(getColumnIndex(column), value);
	}
}
