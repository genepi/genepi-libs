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

public interface ITableWriter {

	public void setColumns(String[] columns);

	public void setInteger(int column, int value);

	public void setInteger(String column, int value);

	public void setDouble(int column, double value);

	public void setDouble(String column, double value);

	public void setString(int column, String value);

	public void setString(String column, String value);

	public int getColumnIndex(String column);
	
	public void setRow(String[] row);

	public boolean next();

	public void close();

}
