/**
 * diewald_shapeFileReader.
 * 
 * a Java Library for reading ESRI-shapeFiles (*.shp, *.dfb, *.shx).
 * 
 * 
 * Copyright (c) 2012 Thomas Diewald
 *
 *
 * This source is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This code is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License is available on the World
 * Wide Web at <http://www.gnu.org/copyleft/gpl.html>. You can also
 * obtain it by writing to the Free Software Foundation,
 * Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */


package diewald_shapeFile.files.shx;

import java.io.File;
import java.nio.ByteOrder;
import java.util.Locale;

import diewald_shapeFile.files.ShapeFileReader;
import diewald_shapeFile.files.shp.SHP_Header;
import diewald_shapeFile.shapeFile.ShapeFile;



/**
 * Shape Index File Reader (*.shx).<br><br>
 * stores offset and length of record-items in a ShapeFile (*.shp).<br>
 * can be used for diretly accessing records.<br>
 * 
 * @author thomas diewald (2012)
 *
 */
public class SHX_File extends ShapeFileReader{
  
  /** enable/disable general info-logging. */
  public static boolean LOG_INFO = true;
  /** enable/disable logging of the header, while loading. */
  public static boolean LOG_ONLOAD_HEADER = true;
  /** enable/disable logging of the content, while loading. */
  public static boolean LOG_ONLOAD_CONTENT = true;
  
  private SHP_Header header;
  private int[] SHX_shape_offsets;
  private int[] SHX_shape_content_lengths;
  
  
  public SHX_File(ShapeFile parent_shapefile, File file) throws Exception{
    super(parent_shapefile, file);
  }
  
  @Override
  public void read() throws Exception{
    // READ HEADER
    header = new SHP_Header(parent_shapefile, file );  
    header.read(bb);
    if( LOG_ONLOAD_HEADER )
      printHeader();
    
    // READ RECORDS
    bb.order(ByteOrder.BIG_ENDIAN);

    int number_of_bytes   = bb.capacity() - bb.position();
    int number_of_ints    = number_of_bytes/4;
    int number_of_records = number_of_ints/2;
    
    SHX_shape_offsets         = new int[number_of_records];
    SHX_shape_content_lengths = new int[number_of_records];
    
    for(int i = 0; i < SHX_shape_offsets.length; i++){
      SHX_shape_offsets[i]         = bb.getInt();
      SHX_shape_content_lengths[i] = bb.getInt();
    }
    if( LOG_ONLOAD_CONTENT )
      printContent();

    if( LOG_INFO )
      System.out.printf("(ShapeFile) loaded File: \"%s\", records=%d\n", file.getName(), SHX_shape_offsets.length);
  }
  
  
  public SHP_Header getHeader(){
    return header;
  }
  
  /**
   * get an array of offset-values (in bytes) that can be used for direct access of *.shp-file record-.items.<br>
   * @return offset-values as int[] array.
   */
  public int[] getRecordOffsets(){
    return SHX_shape_offsets;
  }
  /**
   * get an array of length-values, which indicate the size (in bytes) of *.shp-file record-.items.<br>
   * @return length/size-values as int[] array.
   */
  public int[] getRecordLenghts(){
    return SHX_shape_content_lengths;
  }
  
  
  
  

  @Override
  public void printHeader() {
    header.print();
  }

  @Override
  public void printContent() {
;
    System.out.printf(Locale.ENGLISH, "\n________________________< CONTENT >________________________\n");
    System.out.printf(Locale.ENGLISH, "  FILE: \"%s\"\n", file.getName());
    System.out.printf(Locale.ENGLISH, "\n");
    for(int i = 0; i < SHX_shape_offsets.length; i++){
      System.out.printf("  [%4d] offset(bytes): %8d; record_length(bytes): %8d\n", i, SHX_shape_offsets[i], SHX_shape_content_lengths[i]);
    }
    System.out.printf(Locale.ENGLISH, "________________________< /CONTENT>________________________\n");
  }
}
