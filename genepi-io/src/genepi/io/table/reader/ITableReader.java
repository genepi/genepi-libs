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

package genepi.io.table.reader;

public interface ITableReader extends Iterable<Row>{

	public String[] getColumns();

	public boolean next();

	public double getDouble(String column);

	public double getDouble(int column);

	public String getString(String column);

	public int getInteger(String column);

	public String getString(int column);

	public int getInteger(int column);

	public int getColumnIndex(String column);

	public String[] getRow();
	
	public Row getAsObject();

	public void close();

}
