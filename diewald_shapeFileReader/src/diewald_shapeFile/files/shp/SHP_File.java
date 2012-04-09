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
import java.util.ArrayList;
import java.util.Locale;

import diewald_shapeFile.files.ShapeFileReader;
import diewald_shapeFile.files.shp.shapeTypes.ShpMultiPoint;
import diewald_shapeFile.files.shp.shapeTypes.ShpPoint;
import diewald_shapeFile.files.shp.shapeTypes.ShpPolyLine;
import diewald_shapeFile.files.shp.shapeTypes.ShpPolygon;
import diewald_shapeFile.files.shp.shapeTypes.ShpShape;
import diewald_shapeFile.shapeFile.ShapeFile;






//http://webhelp.esri.com/arcgisdesktop/9.2/index.cfm?TopicName=Shapefile_file_extensions
/**
 * Shape File Reader (*.shp).<br><br>
 * contains geometry/shapes of a certain type (ShpPoint, ShpMultiPoint, ShpPolygon, ShpPolyLine).<br>
 * 
 * @author thomas diewald (2012)
 */
public class SHP_File extends ShapeFileReader{
  
  /** enable/disable general info-logging. */
  public static boolean LOG_INFO = true;
  /** enable/disable logging of the header, while loading. */
  public static boolean LOG_ONLOAD_HEADER = true;
  /** enable/disable logging of the content, while loading. */
  public static boolean LOG_ONLOAD_CONTENT = true;
  
  
  private SHP_Header header;
  private ArrayList<ShpShape> shapes = new ArrayList<ShpShape>(); // works independent of any *.shx file.

  
  public SHP_File( ShapeFile parent_shapefile, File file ) throws Exception{
    super(parent_shapefile, file);
  }


  @Override
  public void read() throws Exception{
    // READ HEADER
    header = new SHP_Header(parent_shapefile, file); 
    header.read(bb);
    
    if( LOG_ONLOAD_HEADER )
      printHeader();
    
    ShpShape.Type shape_type = header.getShapeType();

    // READ CONTENT (depends on the Shape.Type)
    if( shape_type == ShpShape.Type.NullShape ){
      ;// TODO: handle NullShapes
    } else if( shape_type.isTypeOfPolygon   ()) { while( bb.position() != bb.capacity()) shapes.add( new ShpPolygon(shape_type)   .read(bb) );
    } else if( shape_type.isTypeOfPolyLine  ()) { while( bb.position() != bb.capacity()) shapes.add( new ShpPolyLine(shape_type)  .read(bb) );
    } else if( shape_type.isTypeOfPoint     ()) { while( bb.position() != bb.capacity()) shapes.add( new ShpPoint(shape_type)     .read(bb) );
    } else if( shape_type.isTypeOfMultiPoint()) { while( bb.position() != bb.capacity()) shapes.add( new ShpMultiPoint(shape_type).read(bb) );
    } else if (shape_type == ShpShape.Type.MultiPatch){
      System.err.println("(ShapeFile) Shape.Type.MultiPatch not supported at the moment.");
    }

    if( LOG_ONLOAD_CONTENT )
      printContent();
    
    if( LOG_INFO )
//      System.out.println("(ShapeFile) loaded *.shp-File: \""+file.getName()+"\",  shapes="+shapes.size()+"("+shape_type+")");
      System.out.printf("(ShapeFile) loaded File: \"%s\", records=%d (%s-Shapes)\n", file.getName(), shapes.size(), shape_type);
  }
  

  public SHP_Header getHeader(){
    return header;
  }
  /**
   * get the shapes of the file as an ArrayList.<br>
   * <pre>
   * elements can be of type (proper casting!):
   * ShpPoint
   * ShpMultiPoint
   * ShpPolygon
   * ShpPolyLine
   * </pre>
   * @return ArrayList with elements of type: ShpShape
   */
  public ArrayList<ShpShape> getShpShapes(){
    return shapes;
  }

  @Override
  public void printHeader() {
    header.print();
  }


  @Override
  public void printContent() {
    System.out.printf(Locale.ENGLISH, "\n");
    System.out.printf(Locale.ENGLISH, "________________________< CONTENT >________________________\n");
    System.out.printf(Locale.ENGLISH, "  FILE: \"%s\"\n", file.getName());
    System.out.printf(Locale.ENGLISH, "\n");
    for( ShpShape shape: shapes ){
      shape.print();
    }
    System.out.printf(Locale.ENGLISH, "\n");
    System.out.printf(Locale.ENGLISH, "________________________< /CONTENT >________________________\n");
  }
}
