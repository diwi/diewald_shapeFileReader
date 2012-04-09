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


package diewald_shapeFile.files.shp;

import java.io.File;
import java.nio.ByteBuffer;


import java.nio.ByteOrder;
import java.util.Locale;

import diewald_shapeFile.files.shp.shapeTypes.ShpShape;
import diewald_shapeFile.shapeFile.ShapeFile;




/**
 * Shape File Header Reader.<br><br>
 * used for *.shp-files and *.shx-files.<br>
 * contains information about:<br>
 *  ...<br>
 *  Shape-Type<br>
 *  boundingbox<br>
 *  measure-range<br>
 *  ...<br>
 *  
 *  @author thomas diewald (2012)
 */
public class SHP_Header {
  @SuppressWarnings("unused")
  private ShapeFile parent_shapefile;
  private File file;
  private final static int SHP_MAGIC   = 9994;
  private final static int SHP_VERSION = 1000;
  
  private int SHP_file_length, SHP_shape_type;
  
  private double[][] SHP_bbox = new double[3][2]; // [x, y, z][min, max]
  private double[] SHP_range_m = new double[2];   // [min, max]
  
  private ShpShape.Type shape_type = null;
  
  /**
   * generates a new ShapeFileHeader.
   * @param file
   */
  public SHP_Header(ShapeFile parent_shapefile, File file){
    this.parent_shapefile = parent_shapefile;
    this.file = file;
  }
  
  /**
   * read the Header from the given ByteBuffer.
   * @param bb ByteBuffer.
   * @throws Exception
   */
  public void read(ByteBuffer bb) throws Exception{
    // MAIN FILE HEADER
    bb.order(ByteOrder.BIG_ENDIAN);
    // magic number
    int SHP_MAGIC_read   = bb.getInt( 0);
    if( SHP_MAGIC_read != SHP_MAGIC ){
      throw new Exception("(ShapeFile) error: SHP_MAGIC = "+SHP_MAGIC+", File: "+file );
    }
    // file length
    SHP_file_length  = bb.getInt(24);
    bb.order(ByteOrder.LITTLE_ENDIAN);
    int SHP_version_read      = bb.getInt(28);
    if( SHP_version_read != SHP_VERSION ){
      throw new Exception("(ShapeFile) error: SHP_VERSION = "+SHP_VERSION+", File: "+file );
    }
    
    SHP_shape_type = bb.getInt(32);
   
    try {
      shape_type = ShpShape.Type.byID(SHP_shape_type);
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    SHP_bbox[0][0] = bb.getDouble(36); // x-min
    SHP_bbox[1][0] = bb.getDouble(44); // y-min
    SHP_bbox[0][1] = bb.getDouble(52); // x-max
    SHP_bbox[1][1] = bb.getDouble(60); // y-max
    SHP_bbox[2][1] = bb.getDouble(68); // z-min
    SHP_bbox[2][1] = bb.getDouble(76); // z-max
    SHP_range_m[0] = bb.getDouble(84); // m-min
    SHP_range_m[1] = bb.getDouble(92); // m-max
    
    bb.position(100);
  }
  
  /**
   * get the type ShapeType the shapeFile contains.<br>
   * a shapeFile contains only one type of shape.<br>
   * @return ShpShape.Type
   */
  public ShpShape.Type getShapeType(){
    return shape_type;
  }
  /**
   * data storage: [3][2] --> [x,y,z][min, max].
   * @return boundingbox as double[][]
   */
  public double[][] getBoundingBox(){
    return SHP_bbox;
  }
  /**
   * get measure range.<br>
   * data storage: [2] --> [min, max]
   * @return double[]
   */
  public double[] getMeasureRange(){
    return SHP_range_m;
  }
  /**
   * get length in bytes of the shapeFile.
   * @return length in bytes.
   */
  public int getFileLengthBytes(){
    return SHP_file_length;
  }
  /**
   * get Verions on the shapeFile.
   * @return should return 1000.
   */
  public int getVersion(){
    return SHP_VERSION;
  }
  /**
   * get MAGIC NUMBER of shapeFile.
   * @return should return 9994.
   */
  public int getMagicNumber(){
    return SHP_MAGIC;
  }

  
  public void print(){
    System.out.printf(Locale.ENGLISH, "\n");
    System.out.printf(Locale.ENGLISH, "________________________< HEADER >________________________\n");
    System.out.printf(Locale.ENGLISH, "  FILE: \"%s\"\n", file.getName());
    System.out.printf(Locale.ENGLISH, "\n");
    System.out.printf(Locale.ENGLISH, "  SHP_MAGIC               = %d\n", SHP_MAGIC);
    System.out.printf(Locale.ENGLISH, "  SHP_file_length         = %d bytes\n", SHP_file_length*2);
    System.out.printf(Locale.ENGLISH, "  SHP_VERSION             = %d\n", SHP_VERSION);
    System.out.printf(Locale.ENGLISH, "  shape_type              = %s (%d)\n", shape_type, shape_type.ID() );
    System.out.printf(Locale.ENGLISH, "  SHP_bbox: xmin, xmax    = %+7.3f, %+7.3f\n", SHP_bbox[0][0], SHP_bbox[0][1]);
    System.out.printf(Locale.ENGLISH, "  SHP_bbox: ymin, ymax    = %+7.3f, %+7.3f\n", SHP_bbox[1][0], SHP_bbox[1][1]);
    System.out.printf(Locale.ENGLISH, "  SHP_bbox: zmin, zmax    = %+7.3f, %+7.3f\n", SHP_bbox[2][0], SHP_bbox[2][1]);
    System.out.printf(Locale.ENGLISH, "  SHP_measure: mmin, mmax = %+7.3f, %+7.3f\n", SHP_range_m[0], SHP_range_m[1]);
    System.out.printf(Locale.ENGLISH, "________________________</HEADER >________________________\n");
    System.out.printf(Locale.ENGLISH, "\n");
  }
}
