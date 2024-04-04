package com.openclassrooms.hexagonal.games.ui.homefeed;

import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.openclassrooms.hexagonal.games.R;
import com.openclassrooms.hexagonal.games.databinding.ItemPostBinding;
import com.openclassrooms.hexagonal.games.domain.model.Post;
import com.openclassrooms.hexagonal.games.ui.homefeed.HomefeedAdapter.PostViewHolder;

/**
 * This adapter class manages the data displayed in the home feed list. It extends the RecyclerView.Adapter class
 * and provides a ViewHolder class for efficiently displaying Post data. The adapter also supports an interface
 * `OnPostClickListener` for handling user clicks on individual posts.
 */
public final class HomefeedAdapter
    extends Adapter<PostViewHolder>
{

  /**
   * Interface definition for handling clicks on posts in the home feed.
   */
  public interface OnPostClickListener
  {

    /**
     * Callback method invoked when a user clicks on a Post item.
     *
     * @param post The Post object that was clicked on.
     */
    void onClick(Post post);

  }

  /**
   * ViewHolder class representing the view for each Post item in the home feed list.
   */
  public static final class PostViewHolder
      extends ViewHolder
  {

    /**
     * TextView displaying the username of the post author.
     */
    public final TextView username;

    /**
     * TextView displaying the title of the post.
     */
    public final TextView title;

    /**
     * TextView displaying the description of the post (optional).
     */
    public final TextView description;

    /**
     * ImageView displaying the post's image (optional).
     */
    public final ImageView image;

    /**
     * Constructor for PostViewHolder.
     *
     * @param binding The ItemPostBinding object used for data binding.
     */
    public PostViewHolder(@NonNull ItemPostBinding binding)
    {
      super(binding.getRoot());

      this.username = binding.userName;
      this.title = binding.title;
      this.description = binding.description;
      this.image = binding.image;
    }

    /**
     * Binds data from a Post object to the ViewHolder's UI elements and sets up click listener.
     *
     * @param post          The Post object representing the data to be displayed.
     * @param clickListener The OnPostClickListener instance for handling clicks.
     */
    public void bind(Post post, OnPostClickListener clickListener)
    {
      username.setText(itemView.getContext().getString(R.string.by, post.author.firstname, post.author.lastname));

      title.setText(post.title);

      description.setText(post.description);
      description.setVisibility(TextUtils.isEmpty(post.description) == false ? View.VISIBLE : View.GONE);

      if (TextUtils.isEmpty(post.photoUrl) == false)
      {
        Glide
            .with(image.getContext())
            .load(post.photoUrl)
            .placeholder(new ColorDrawable(Color.GRAY))
            .into(image);

        image.setVisibility(View.VISIBLE);
      }
      else
      {
        image.setVisibility(View.GONE);
      }

      itemView.setOnClickListener(v -> {
        clickListener.onClick(post);
      });

    }
  }

  /**
   * A list of Post objects representing the data displayed in the home feed.
   */
  @NonNull
  private List<Post> posts;

  /**
   * An instance of the OnPostClickListener interface for handling post clicks.
   */
  @NonNull
  private OnPostClickListener onPostClickListener;

  /**
   * Constructor for HomefeedAdapter.
   *
   * @param posts               A list of Post objects to be displayed.
   * @param onPostClickListener The OnPostClickListener instance for handling clicks.
   */
  public HomefeedAdapter(@NonNull List<Post> posts,
      @NonNull OnPostClickListener onPostClickListener)
  {
    this.posts = posts;
    this.onPostClickListener = onPostClickListener;
  }

  @NonNull
  @Override
  public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
  {
    final ItemPostBinding binding = ItemPostBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    return new PostViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull PostViewHolder holder, int position)
  {
    holder.bind(posts.get(position), onPostClickListener);
  }

  @Override
  public int getItemCount()
  {
    return posts.size();
  }

  /**
   * Updates the adapter's data set with a new list of Post objects.
   * <p>
   * This method replaces the existing list of posts held by the adapter with the provided list.
   * It then triggers a notification to the RecyclerView that the data set has changed,
   * which prompts the adapter to refresh the UI accordingly.
   * </p>
   *
   * @param posts The new list of Post objects to be displayed.
   */
  public void update(List<Post> posts)
  {
    this.posts = posts;
    notifyDataSetChanged();
  }

}
