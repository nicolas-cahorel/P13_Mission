package com.openclassrooms.hexagonal.games.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.openclassrooms.hexagonal.games.R;
import com.openclassrooms.hexagonal.games.databinding.ActivityMainBinding;
import dagger.hilt.android.AndroidEntryPoint;

/**
 * Main activity for the application. This activity serves as the entry point and container for the navigation
 * fragment. It handles setting up the toolbar, navigation controller, and action bar behavior.
 */
@AndroidEntryPoint
public final class MainActivity
    extends AppCompatActivity
{

  /**
   * Configuration object for the AppBar in the activity.
   */
  private AppBarConfiguration appBarConfiguration;

  /**
   * View binding object for the activity's layout (activity_main.xml).
   */
  private ActivityMainBinding binding;

  /**
   * Navigation controller instance for managing app navigation.
   */
  private NavController navController;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    setSupportActionBar(binding.toolbar);

    navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
    appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
  }

  @Override
  public boolean onSupportNavigateUp()
  {
    return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
  }

}
