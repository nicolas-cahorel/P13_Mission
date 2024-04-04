package com.openclassrooms.hexagonal.games.ui.homefeed;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.State;

/**
 * This class provides item decoration for the RecyclerView in the HomefeedFragment.
 * It adds spacing between items in the list for better visual presentation.
 */
public final class HomefeedItemDecoration
    extends ItemDecoration
{

  /**
   * The size of the space to be added between items in the RecyclerView (in pixels).
   */
  private final int spaceSize;

  /**
   * Constructor for HomefeedItemDecoration.
   *
   * @param spaceSize The size of the spacing to be added between items (pixels).
   */
  public HomefeedItemDecoration(int spaceSize)
  {
    this.spaceSize = spaceSize;
  }

  @Override
  public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
      @NonNull RecyclerView parent, @NonNull State state)
  {
    if (parent.getChildAdapterPosition(view) == 0)
    {
      outRect.top = spaceSize;
    }

    outRect.left = spaceSize;
    outRect.right = spaceSize;
    outRect.bottom = spaceSize;
  }

}
