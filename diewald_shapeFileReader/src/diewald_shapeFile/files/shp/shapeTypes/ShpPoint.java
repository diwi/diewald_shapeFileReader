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


package diewald_shapeFile.files.shp.shapeTypes;

import java.nio.ByteBuffer;
import java.util.Locale;

/**
 * Shape: Point.<br>
 * <pre>
 * possible ShapeTypes:
 *   Point   (  1 ), 
 *   PointZ  ( 11 ), 
 *   PointM  ( 21 ), 
 * </pre>
 * 
 * @author thomas diewald (2012)
 *
 */

public class ShpPoint extends ShpShape{
  
  // SHAPE RECORD CONTENT
  private double[] SHP_xyz = new double[3]; // [x,y,z]
  private double SHP_m_value; //[m-value]

  public ShpPoint(ShpShape.Type shape_type){
    super(shape_type);
  }
  
  @Override
  protected void readRecordContent(ByteBuffer bb){
    SHP_xyz[0] = bb.getDouble(); // x - coordinate
    SHP_xyz[1] = bb.getDouble(); // y - coordinate
    
    // if SHAPE-TYPE: 11
    if( shape_type.hasZvalues() ){
      SHP_xyz[2] = bb.getDouble(); // z - coordinate
    }
    // if SHAPE-TYPE: 11 | 21
    if( shape_type.hasMvalues() ){
      SHP_m_value = bb.getDouble(); // m - value
    }
  }
  
  @Override 
  public void print(){
//    System.out.printf(Locale.ENGLISH, "\n");
//    System.out.printf(Locale.ENGLISH, "  __________________< SHAPE >__________________\n");
//    System.out.printf(Locale.ENGLISH, "    <RECORD HEADER>\n");
//    System.out.printf(Locale.ENGLISH, "      SHP_record_number       = %d\n", SHP_record_number);
//    System.out.printf(Locale.ENGLISH, "      SHP_content_length      = %d bytes  (check: start/end/size = %d/%d/%d)\n", SHP_content_length*2, position_start, position_end, content_length);
//    System.out.printf(Locale.ENGLISH, "    <RECORD CONTENT>\n");
//    System.out.printf(Locale.ENGLISH, "      shape_type              = %s (%d)\n", shape_type, shape_type.ID() );
//    System.out.printf(Locale.ENGLISH, "    __________________</SHAPE >__________________\n");
    
    
    System.out.printf(Locale.ENGLISH, "   _ _ _ _ _ \n");
    System.out.printf(Locale.ENGLISH, "  / SHAPE   \\_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\n");
    System.out.printf(Locale.ENGLISH, "  |                                                    \\\n");
    System.out.printf(Locale.ENGLISH, "  |  <RECORD HEADER>\n");
    System.out.printf(Locale.ENGLISH, "  |    SHP_record_number       = %d\n", SHP_record_number);
    System.out.printf(Locale.ENGLISH, "  |    SHP_content_length      = %d bytes  (check: start/end/size = %d/%d/%d)\n", SHP_content_length*2, position_start, position_end, content_length);
    System.out.printf(Locale.ENGLISH, "  |\n");
    System.out.printf(Locale.ENGLISH, "  |  <RECORD CONTENT>\n");
    System.out.printf(Locale.ENGLISH, "  |    shape_type              = %s (%d)\n", shape_type, shape_type.ID() );
    System.out.printf(Locale.ENGLISH, "  |    x,y,z,m                 = %5.2f, %5.2f, %5.2f, %5.2f\n", SHP_xyz[0], SHP_xyz[1], SHP_xyz[2], SHP_m_value );
    System.out.printf(Locale.ENGLISH, "  \\_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ /\n");
  }
  
  
  
  
  
  
  
  public double[] getPoint(){
    return this.SHP_xyz;
  }
  public double getMeasure(){
    return this.SHP_m_value;
  }
  
//  /**
//   * get range of Measure-Values.<br>
//   * data storage: [min, max]  <br>
//   * @return 1d-array (double), dim-size:[2]
//   */
//  public double[] getMeasureRange(){
//    return SHP_range_m;
//  }
  
  
//  /**
//   * get the number of points(vertices).
//   * @return the number of points(vertices).
//   */
//  public int getNumberOfPoints(){
//    return SHP_num_points;
//  }
//  /**
//   * get the number of parts(Polygons)
//   * @return the number of parts(Polygons).
//   */
//  public int getNumberOfParts(){
//    return SHP_num_parts;
//  }
//  
//  /**
//   * get an array of all points(vertices).
//   * @return an array of all points(vertices).
//   */
//  public double[][] getPoints(){
//    return SHP_xyz_points;
//  }
  

//  /**
//   * generates a list of polygons, and returns a 3d-double array.<br>
//   * [number of polygons][number of points per polygon][x, y, z, m].
//   * 
//   * @return 3d-double array.
//   */
//  public double[][][] getPointsAs3DArray_CPY(){
//
//    int[] indices = new int[SHP_num_parts+1];                  // generate new indices array
//    System.arraycopy(SHP_parts, 0, indices, 0, SHP_num_parts); // copy start indices
//    indices[indices.length-1] = SHP_num_points;                // and add last index
//    
//    double[][][] parts = new double[SHP_num_parts][][];
//    for(int i = 0; i < indices.length-1; i++){
//      int from = indices[i];   // start index
//      int to   = indices[i+1]; // end-index + 1
//      int size = to-from;
//      parts[i] = new double[size][4];
//      for(int j = from, idx = 0; j < to; j++, idx++){
//        parts[i][idx][0] = SHP_xyz_points[j][0]; // copy of x-value
//        parts[i][idx][1] = SHP_xyz_points[j][1]; // copy of y-value
//        parts[i][idx][2] = SHP_xyz_points[j][2]; // copy of z-value
//        if( shape_type.hasMvalues() ){
//          parts[i][idx][3] = SHP_m_values[j];      // copy of m-value
//        }
//      }
//    }
//    return parts;
//  }
  
  
//  /**
//   * get the Measure Values as an Array.
//   * @return measure-values. (size=.getNumberOfPoints()).
//   */
//  public double[] getMeasureValues(){
//    return SHP_m_values;
//  }
//  
  

}
