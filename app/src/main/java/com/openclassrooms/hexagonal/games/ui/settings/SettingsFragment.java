package com.openclassrooms.hexagonal.games.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.hexagonal.games.databinding.FragmentSettingsBinding;
import dagger.hilt.android.AndroidEntryPoint;

/**
 * This fragment represents the Settings screen in the application. It allows users to enable or disable notifications.
 */
@AndroidEntryPoint
public final class SettingsFragment
    extends Fragment
{

  /**
   * View binding object for the fragment's layout (fragment_settings.xml).
   */
  private FragmentSettingsBinding binding;

  /**
   * ViewModel responsible for handling data and events related to application settings.
   */
  private SettingsViewModel viewModel;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState)
  {
    binding = FragmentSettingsBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
  {
    super.onViewCreated(view, savedInstanceState);

    setupUI();
    setupViewModel();
  }

  @Override
  public void onDestroyView()
  {
    super.onDestroyView();
    binding = null;
  }

  /**
   * Retrieves the SettingsViewModel instance for this fragment.
   */
  private void setupViewModel()
  {
    viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
  }

  /**
   * Initializes UI elements and sets up click listeners for the notification toggle buttons.
   */
  private void setupUI()
  {
    binding.notificationEnable.setOnClickListener(v -> {
      viewModel.enableNotifications();
    });

    binding.notificationDisable.setOnClickListener(v -> {
      viewModel.disableNotifications();
    });
  }

}