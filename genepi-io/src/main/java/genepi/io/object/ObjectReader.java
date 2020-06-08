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

package genepi.io.object;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class ObjectReader {

	private String filename;
	private Class c;

	public ObjectReader(String filename, Class c) {
		this.filename = filename;
		this.c = c;
	}

	public List<Object> readAsList() {
		List<Object> result = new Vector<Object>();
		try {
			FileInputStream fileInputStream = new FileInputStream(filename);
			DataInput in = new DataInputStream(fileInputStream);
			while (fileInputStream.available() > 0) {
				IReadableObject o;
				o = (IReadableObject) c.newInstance();
				o.read(in);
				result.add(o);

			}

		} catch (EOFException e) {
			// eof
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

}
