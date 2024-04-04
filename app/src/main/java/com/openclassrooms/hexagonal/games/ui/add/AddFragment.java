package com.openclassrooms.hexagonal.games.ui.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.hexagonal.games.databinding.FragmentAddBinding;
import dagger.hilt.android.AndroidEntryPoint;

/**
 * This fragment represents the Add screen in the application. It allows users to create new posts.
 * It utilizes ViewModel for managing data and interactions.
 *
 * @see AddViewModel
 */

@AndroidEntryPoint
public final class AddFragment
    extends Fragment
{

  /**
   * View binding object for the fragment's layout (fragment_add.xml).
   */
  private FragmentAddBinding binding;

  /**
   * ViewModel instance responsible for handling data and interactions related to adding posts.
   */
  private AddViewModel viewModel;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState)
  {
    binding = FragmentAddBinding.inflate(inflater, container, false);
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
   * Initializes the ViewModel instance using ViewModelProvider.
   */
  private void setupViewModel()
  {
    viewModel = new ViewModelProvider(this).get(AddViewModel.class);
  }

  /**
   * Sets up the UI elements of the fragment (button clicks, text input handling etc.).
   * Specific implementation details for UI setup omitted here for brevity.
   */
  private void setupUI()
  {
  }

}