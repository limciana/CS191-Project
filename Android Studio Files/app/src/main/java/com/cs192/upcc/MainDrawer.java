/*
 * This is a course requirement for CS 192 Software Engineering II
 * under the supervision of Asst. Prof. Ma. Rowena C. Solamo
 * of the Department of Computer Science, College of Engineering,
 * University of the Philippines, Diliman
 * for the AY 2017-2018.
 * This code is written by Rayven Ely Cruz.
 */

/* Code History
 * Programmer           Date     Description
 * Rayven Ely Cruz      2/18/18  .
 */

/*
 * File Creation Date: 1/27/18
 * Development Group: James Abaja, Rayven Cruz, Ciana Lim
 * Client Group: CS 192 Class
 * Purpose of the Software: To aid the DCS students in tracking their taken subjects, and the subjects they can take afterwards.
 */
package com.cs192.upcc;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SelectCurriculumFragment.OnDataPass, InputSubjectFragment.OnDataPass {
     Curriculum curriculum;
     boolean doubleBackToExitPressedOnce;
     DrawerLayout drawer;
     @Override
     protected void onCreate(Bundle savedInstanceState) {
          setTheme(R.style.AppTheme_NoActionBar);
          doubleBackToExitPressedOnce = false;
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_main_drawer);
          Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
          setSupportActionBar(toolbar);
          /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
          fab.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
               }
          }); */

          drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

          ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                  this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
          drawer.addDrawerListener(toggle);
          toggle.syncState();

          NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
          navigationView.setNavigationItemSelectedListener(this);

          /* Attach SelectCurriculumFragment */
          android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
          android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
          SelectCurriculumFragment selectCurriculumFragment = new SelectCurriculumFragment();
          fragmentTransaction.add(R.id.fragContainer, selectCurriculumFragment);
          fragmentTransaction.commit();


     }

     @Override
     public void onDataPass(String data) {
          Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
          toolbar.setTitle(data);
          setSupportActionBar(toolbar);
     }
     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
          // Inflate the menu; this adds items to the action bar if it is present.
          getMenuInflater().inflate(R.menu.main_drawer, menu);
          return true;
     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
          // Handle action bar item clicks here. The action bar will
          // automatically handle clicks on the Home/Up button, so long
          // as you specify a parent activity in AndroidManifest.xml.
          int id = item.getItemId();
          if(drawer.isDrawerOpen(Gravity.START)){
               drawer.closeDrawer(Gravity.START);
          } else {
               drawer.openDrawer(Gravity.START);
          }
          //noinspection SimplifiableIfStatement
          if (id == R.id.action_settings) {
               return true;
          }

          return super.onOptionsItemSelected(item);
     }

     @SuppressWarnings("StatementWithEmptyBody")
     @Override
     public boolean onNavigationItemSelected(MenuItem item) {
          // Handle navigation view item clicks here.
          int id = item.getItemId();

          if (id == R.id.nav_camera) {
               // Handle the camera action

          } else if (id == R.id.nav_gallery) {

          } else if (id == R.id.nav_slideshow) {

          } else if (id == R.id.nav_manage) {

          } else if (id == R.id.nav_share) {

          } else if (id == R.id.nav_send) {

          }

          DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
          drawer.closeDrawer(GravityCompat.START);
          return true;
     }

     /*
   * Name: onBackPressed
   * Creation Date: 2/19/18
   * Purpose: double tap to back
   * Arguments:
   *      none
   * Other Requirements:
   *      doubleBackToExitPressedOnce
   * Return Value: void
   */
     @Override
     public void onBackPressed() {
          if (doubleBackToExitPressedOnce) {
               super.onBackPressed();
               return;
          }

          this.doubleBackToExitPressedOnce = true;
          Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

          new Handler().postDelayed(new Runnable() {

               @Override
               public void run() {
                    doubleBackToExitPressedOnce = false;
               }
          }, 2000);
     }
}
