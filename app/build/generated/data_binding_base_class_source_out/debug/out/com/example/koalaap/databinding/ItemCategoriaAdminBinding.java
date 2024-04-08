// Generated by view binder compiler. Do not edit!
package com.example.koalaap.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.koalaap.R;
import com.google.android.material.card.MaterialCardView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ItemCategoriaAdminBinding implements ViewBinding {
  @NonNull
  private final MaterialCardView rootView;

  @NonNull
  public final ImageButton EliminarCat;

  @NonNull
  public final TextView ItemNombreCatA;

  private ItemCategoriaAdminBinding(@NonNull MaterialCardView rootView,
      @NonNull ImageButton EliminarCat, @NonNull TextView ItemNombreCatA) {
    this.rootView = rootView;
    this.EliminarCat = EliminarCat;
    this.ItemNombreCatA = ItemNombreCatA;
  }

  @Override
  @NonNull
  public MaterialCardView getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemCategoriaAdminBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemCategoriaAdminBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_categoria_admin, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemCategoriaAdminBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.Eliminar_cat;
      ImageButton EliminarCat = ViewBindings.findChildViewById(rootView, id);
      if (EliminarCat == null) {
        break missingId;
      }

      id = R.id.Item_nombre_cat_a;
      TextView ItemNombreCatA = ViewBindings.findChildViewById(rootView, id);
      if (ItemNombreCatA == null) {
        break missingId;
      }

      return new ItemCategoriaAdminBinding((MaterialCardView) rootView, EliminarCat,
          ItemNombreCatA);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}