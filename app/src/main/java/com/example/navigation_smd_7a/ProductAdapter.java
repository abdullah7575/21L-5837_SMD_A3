package com.example.navigation_smd_7a;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<Product> {
    Context context;
    int resource;
    int tab;

    // creating interface to have communication between mainactivity related work and fragment related

    public interface ProductScheduledInterface{
        void productScheduled();
        void delieveredProduct();
//        void newOrderProduct();
    }
    ProductScheduledInterface productScheduledInterface;

    public ProductAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Product> objects,int tab) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.tab = tab;
        this.productScheduledInterface = (ProductScheduledInterface) context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if(v == null)
        {
            v = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        switch (tab){
            case 0:
            {

            }
            case 1:
            {

            }
            case 2:
            {
                // we have to make the ids etc diff otherwise same listener working in all tabs for del and update
                // here as the same list i used in all tabs then obv their icons ids are same so i have attached listeners to them so that's why same working of them in all tabs.
                TextView tvTitle = v.findViewById(R.id.tvProductTitle);
                TextView tvPrice = v.findViewById(R.id.tvProductPrice);
                TextView tvDate = v.findViewById(R.id.tvProductDate);
                ImageView ivEdit = v.findViewById(R.id.ivEdit);
                ImageView ivDelete = v.findViewById(R.id.ivDelete);

                Product p = getItem(position);
//                tvTitle.setText(p.getPrice()+" : "+p.getTitle()+":"+p.getDate());
                tvTitle.setText(p.getTitle());
                tvPrice.setText(String.valueOf(p.getPrice()));
                tvDate.setText(String.valueOf(p.getDate()));

                ivEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setTitle("Product");
                        View cv = LayoutInflater.from(context)
                                .inflate(R.layout.update_dialog_design, null, false);
                        dialog.setView(cv);
                        // Initialize dialog components
                        EditText etUpdatedTitle = cv.findViewById(R.id.etUpdatedTitle);
                        EditText etUpdatedDate = cv.findViewById(R.id.etUpdatedDate);
                        EditText etUpdatedPrice = cv.findViewById(R.id.etUpdatedPrice);

                        // Populate fields with the current item values
                        etUpdatedTitle.setText(p.getTitle());
                        etUpdatedDate.setText(p.getDate());
                        etUpdatedPrice.setText(String.valueOf(p.getPrice()));

                        dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String title = etUpdatedTitle.getText().toString().trim();
                                String date = etUpdatedDate.getText().toString().trim();
                                String price = etUpdatedPrice.getText().toString();
                                int id = p.getId();
                                ProductDB productDB = new ProductDB(context);
                                productDB.open();
                                int result = productDB.updateProduct(id,title, date, Integer.parseInt(price));
                                productDB.close();
//                                Toast.makeText(context, "Product Added", Toast.LENGTH_SHORT).show();
//                                productScheduledInterface.newOrderProduct();
                                if(result > 0) {
                                    Toast.makeText(context, "Product updated successfully", Toast.LENGTH_SHORT).show();
//                                    refreshProductList(); // Refresh the UI, if needed
//                                    productScheduledInterface.newOrderProduct();
                                    notifyDataSetChanged();

                                } else {
                                    Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        dialog.show();

                    }
                });
                ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ProductDB db = new ProductDB(context);
                        db.open();
                        db.remove(p.getId());
                        db.close();
                        remove(p);
                        notifyDataSetChanged();
                    }
                });
                // Long press on the entire item view
                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        // Show dialog with options
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Update Status")
                                .setItems(new CharSequence[]{"Delivered", "Scheduled"}, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {
                                        String status = (which == 0) ? "Delivered" : "Scheduled";
                                        // Update the product status in the database
                                        ProductDB db = new ProductDB(context);
                                        db.open();
                                        int result = db.updateStatus(p.getId(), status); // Assumes `updateStatus` method exists in `ProductDB`
                                        db.close();
                                        if(status.equals("Scheduled")){
                                            productScheduledInterface.productScheduled();
                                        }
                                        if(status.equals("Delivered")){
                                            productScheduledInterface.delieveredProduct();
                                        }
                                        if(result > 0) {
                                            Toast.makeText(context, "Status updated successfully", Toast.LENGTH_SHORT).show();
                                            // calling here implementation inside mainActivity
                                        } else {
                                            Toast.makeText(context, "Failed to update status", Toast.LENGTH_SHORT).show();
                                        }

//                                        Toast.makeText(context, "Status updated to " + status, Toast.LENGTH_SHORT).show();
                                       // check this to do...
                                        notifyDataSetChanged();  // Refresh the list to show the updated status if needed
                                    }
                                })
                                .setNegativeButton("Cancel", null);

                        builder.show();
                        return true;  // Indicates the long press was handled
                    }
                });
                return v;
            }
        }

        return null;


    }
}
