/*
 * This is a course requirement for CS 192 Software Engineering II
 * under the supervision of Asst. Prof. Ma. Rowena C. Solamo
 * of the Department of Computer Science, College of Engineering,
 * University of the Philippines, Diliman
 * for the AY 2017-2018.
 * This code is written by Ciana Lim.
 */

/* Code History
 * Programmer     Date     Description
 * Ciana Lim      1/27/18  Created the file to set up the database
 * Ciana Lim      1/28/18  Modified the database to accept pre-seeded data.
 * Ciana Lim      1/31/18  Included methods to get the curriculums, and the subjects from the selected curriculum, from the database.
 * Ciana Lim      2/4/18   Added citations.
 * Ciana Lim      2/14/18  Added insertion of passed subjects and deletion of wrongly marked subjects to the student's database for non-volatility.
 * Ciana Lim      3/6/18   Added deleteAllStudentData method
 */

/*
 * File Creation Date: 1/27/18
 * Development Group: James Abaja, Rayven Cruz, Ciana Lim
 * Client Group: CS 192 Class
 * Purpose of the Software: To aid the DCS students in tracking their taken subjects, and the subjects they can take afterwards.
 */

/* 
 * Citations
 * ProgrammingKnowledge. Android SQLite Database Tutorial 1 # Introduction + Creating Database and Tables (Part 1). Last accessed: February 4, 2018
 * ProgrammingKnowledge. Android SQLite Database Tutorial 2 # Introduction + Creating Database and Tables (Part 2). Last accessed: February 4, 2018
 * ProgrammingKnowledge. Android SQLite Database Tutorial 4 # Show SQLite Database table Values using Android. Last accessed: February 4, 2018
 * Gilfelt, Jeff. android-sqlite-asset-helper. Last accessed: February 4, 2018
 * ProgrammingKnowledge. Android SQLite Database Tutorial 3 # Insert values to SQLite Database table using Android. Last accessed: February 14, 2018
 * ProgrammingKnowledge. Android SQLite Database Tutorial 6 # Delete values in SQLite Database table using Android. Last accessed: February 14, 2018
 */

package com.cs192.upcc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseHelper extends SQLiteAssetHelper {
     public static final String DATABASE_NAME = "UPCC.db"; // the database name
     public static final String TABLE_1 = "subjects_table"; // the name of table 1
     public static final String TABLE_1_COL_1 = "CURRICULUM"; // the name of table 1 column 1
     public static final String TABLE_1_COL_2 = "SUBJECT_NAME"; // the name of table 1 column 2
     public static final String TABLE_1_COL_3 = "SUBJECT_DESCRIPTION"; // the name of table 1 column 3
     public static final String TABLE_1_COL_4 = "UNITS"; // the name of table 1 column 4
     public static final String TABLE_1_COL_5 = "IS_JS"; // the name of table 1 column 5
     public static final String TABLE_1_COL_6 = "IS_SS"; // the name of table 1 column 6
     public static final String TABLE_1_COL_7 = "YEAR_TO_BE_TAKEN"; // the name of table 1 column 7
     public static final String TABLE_1_COL_8 = "PREREQUISITES"; // the name of table 1 column 8
     public static final String TABLE_1_COL_9 = "COREQUISITES"; // the name of table 1 column 9
     public static final String TABLE_2 = "student_table"; // the name of table 2
     public static final String TABLE_2_COL_1 = "CURRICULUM"; // the name of table 2 column 1
     public static final String TABLE_2_COL_2 = "SUBJECT_NAME"; // the name of table 2 column 2
     public static final String TABLE_3 = "prescribed_yearly_units_table"; // the name of table 3
     public static final String TABLE_3_COL_1 = "CURRICULUM"; // the name of table 3 column 1
     public static final String TABLE_3_COL_2 = "YEAR"; // the name of table 3 column 2
     public static final String TABLE_3_COL_3 = "TOTAL_UNITS_NEEDED"; // the name of table 3 column 3
     public static final int VERSION = 3; // the version number of the database
     SQLiteDatabase sqLiteDatabase; // the SQLite database instance
     ContentValues contentValues; // stores the data that is to be inserted to the database
     long result; // checker to see if the data was inserted to the database
     Cursor res; // the resulting rows selected from the query

     public DatabaseHelper(Context context){
          super(context, DATABASE_NAME, null, VERSION);
     }

     /*
      * Name: createDB
      * Creation Date: 1/28/18
      * Purpose: Instantiates the pre-seeded database and makes it available for use.
      * Arguments: none
      * Other Requirements:
      *   sqLiteDatabase - SQLiteDatabase, the SQLite database instance used
      * Return Value: void
      */
     public void createDB(){
          sqLiteDatabase = this.getWritableDatabase();
     }

     /*
      * Name: getAllData
      * Creation: 1/31/18
      * Purpose: Selects all the stored data in the database
      * Arguments: none
      * Other requirements:
      *   sqLiteDatabase - SQLiteDatabase, the SQLite database instance used
      * Return Value: res - Cursor, this interface provides random read-write access to the result set returned by a database query.
      */
     public Cursor getAllData(){
          sqLiteDatabase = this.getWritableDatabase();
          res = sqLiteDatabase.rawQuery("select * from "+ TABLE_1, null);
          return res;
     }

     /* Name: getStudentData
      * Creation: 2/14/18
      * Purpose: Selects all the data of the student
      * Arguments: none
      * Other requirements:
      *   sqLiteDatabase - SQLiteDatabase, the SQLite database instance used
      * Return Value: res - Cursor, this interface provides random read-write access to the result set returned by a database query.
      */
     public Cursor getStudentData(){
          sqLiteDatabase = this.getWritableDatabase();
          res = sqLiteDatabase.rawQuery("select * from "+ TABLE_2, null);
          return res;
     }

     /*
      * Name: getCurriculum
      * Creation: 1/31/18
      * Purpose: Selects the curriculums in the database
      * Arguments: none
      * Other requirements:
      *   sqLiteDatabase - SQLiteDatabase, the SQLite database instance used
      * Return Value: res - Cursor, this interface provides random read-write access to the result set returned by a database query.
      */
     public Cursor getCurriculum(){
          sqLiteDatabase = this.getWritableDatabase();
          res = sqLiteDatabase.rawQuery("select distinct " + TABLE_1_COL_1 + " from " + TABLE_1 + " order by " + TABLE_1_COL_1 + " asc", null);
          return res;
     }

     /*
      * Name: getSubjects
      * Creation: 1/31/18
      * Purpose: Selects all the stored data in the database
      * Arguments:
      *   curriculum - String, the curriculum selected by the user
      * Other requirements:
      *   sqLiteDatabase - SQLiteDatabase, the SQLite database instance used
      * Return Value: res - Cursor, this interface provides random read-write access to the result set returned by a database query.
      */
     public Cursor getSubjects(String curriculum){
          sqLiteDatabase = this.getWritableDatabase();
          res = sqLiteDatabase.rawQuery("select * from " + TABLE_1 + " where " + TABLE_1_COL_1 + " = \"" + curriculum + "\"", null);
          return res;
     }



     /*
      * Name: onCreate
      * Creation Date: 1/27/18
      * Purpose: Creates the tables for the database
      * Arguments:
      *      sqLiteDatabase - SQLiteDatabase, the SQLite database instance used
      * Other Requirements: none
      * Return Value: void
      */
     /*@Override
     public void onCreate(SQLiteDatabase sqLiteDatabase){ // creates table here
          sqLiteDatabase.execSQL("create table "+TABLE_1+"("+TABLE_1_COL_1+" TEXT, "+TABLE_1_COL_2+" TEXT, "+TABLE_1_COL_3+" TEXT, "+TABLE_1_COL_4+" INT, "+TABLE_1_COL_5+" INT, "+TABLE_1_COL_6+" INT, "+TABLE_1_COL_7+" INT, "+TABLE_1_COL_8+" TEXT, "+TABLE_1_COL_9+" TEXT);");
          sqLiteDatabase.execSQL("create table "+TABLE_2+"("+TABLE_2_COL_1+" TEXT, "+TABLE_2_COL_2+" TEXT);");
          sqLiteDatabase.execSQL("create table "+TABLE_3+"("+TABLE_3_COL_1+" TEXT, "+TABLE_3_COL_2+" INT, "+TABLE_3_COL_3+" INT);");
     }*/

     /*
      * Name: onUpgrade
      * Creation Date: 1/27/18
      * Purpose: Drops the tables, if it was modified and the version of the database is changed
      * Arguments:
      *      sqLiteDatabase - SQLiteDatabase, the SQLite database instance used
      *      i - int, the old version of the database
      *      il - int, the new version of the database
      * Other Requirements: none
      * Return Value: void
      */
     /*@Override*/
     /*public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1){
          sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_1+";");
          sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_2+";");
          sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_3+";");
          onCreate(sqLiteDatabase);
     }*/

     /*
      * Name: insertData
      * Creation date: 2/14/18
      * Purpose: Inserts the passed subjects of the student to the database's student table
      * Arguments:
      *   curriculum - String, the name of the curriculum
      *   subject_name - String, the name of the subject that the student passed
      * Other requirements:
      *   sqLiteDatabase - SQLiteDatabase, the SQLite database instance used
      *   contentValues - ContentValues, a set of values that can store the data to be inserted to the database
      * Return value: boolean
      */
     public boolean insertData(String curriculum, String subject_name){
          sqLiteDatabase = this.getWritableDatabase();
          contentValues = new ContentValues();
          contentValues.put(TABLE_2_COL_1, curriculum);
          contentValues.put(TABLE_2_COL_2, subject_name);
          result = sqLiteDatabase.insert(TABLE_2, null, contentValues);
          if(result ==  -1){
               return false;
          }
          else{
               return true;
          }
     }

     /*
      * Name: searchStudentData
      * Creation date: 2/14/18
      * Purpose: Searches the student table to check if the subject already exists
      * Arguments:
      *   curriculum - String, the name of the curriculum
      *   subject_name - String, the name of the subject that the student passed
      * Other requirements:
      *   sqLiteDatabase - SQLiteDatabase, the SQLite database instance used
      * Return value: res - Cursor, this interface provides random read-write access to the result set returned by a database query.
      */
     public Cursor searchStudentData(String curriculum, String subject_name){
          sqLiteDatabase = this.getWritableDatabase();
          res = sqLiteDatabase.rawQuery("select * from " + TABLE_2 + " where " + TABLE_2_COL_1 + " = \"" + curriculum + "\" and " + TABLE_2_COL_2 + " = \"" + subject_name + "\"", null);
          return res;
     }

     /*
      * Name: deleteData
      * Creation date: 2/14/18
      * Purpose: Deletes the row in the student's table based on the curriculum and the subject name
      * Arguments:
      *   curriculum - String, the name of the curriculum
      *   subject_name - String, the name of the subject that the student passed
      * Other requirements:
      *   sqLiteDatabase - SQLiteDatabase, the SQLite database instance used
      * Return value: integer - the number of rows that can be deleted
      */
     public Integer deleteData(String curriculum, String subject_name){
          sqLiteDatabase = this.getWritableDatabase();
          return sqLiteDatabase.delete(TABLE_2, TABLE_2_COL_1 + " = \"" + curriculum + "\" and " + TABLE_2_COL_2 + " = \"" + subject_name + "\"", null);
     }

     /*
      * Name: deleteData
      * Creation date: 3/6/18
      * Purpose: Deletes all entries in the student table
      * Arguments: none
      * Other requirements:
      *   sqLiteDatabase - SQLiteDatabase, the SQLite database instance used
      * Return value: integer - the number of rows that were deleted
      */
     public Integer deleteAllStudentData(){
          sqLiteDatabase = this.getWritableDatabase();
          return sqLiteDatabase.delete(TABLE_2, null, null);
     }

}
