package com.example.navigation_smd_7a;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity implements ProductAdapter.ProductScheduledInterface {

    TabLayout tabLayout;
    ViewPager2 vp2;
    ViewPagerAdapter adapter;
    int count=0;
    boolean flag = false;
    int delieveredCount = 0;
    boolean delieveredFlag = false;
    int newOrderCount = 0;
    boolean newOrderFlag = false;

    FloatingActionButton fab_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        adapter = new ViewPagerAdapter(this);
        vp2 = findViewById(R.id.viewpager2);
        vp2.setAdapter(adapter);
        tabLayout = findViewById(R.id.tabLayout);
        fab_add = findViewById(R.id.fab_add);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Product");
                View v = LayoutInflater.from(MainActivity.this)
                        .inflate(R.layout.add_new_product_dialog_design, null, false);
                dialog.setView(v);
                EditText etTitle = v.findViewById(R.id.etTitle);
                EditText etDate = v.findViewById(R.id.etDate);
                EditText etPrice = v.findViewById(R.id.etPrice);

                dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String title = etTitle.getText().toString().trim();
                        String date = etDate.getText().toString().trim();
                        String price = etPrice.getText().toString();

                        ProductDB productDB = new ProductDB(MainActivity.this);
                        productDB.open();
                        productDB.insert(title, date, Integer.parseInt(price));
                        productDB.close();
                        Toast.makeText(MainActivity.this, "Product Added", Toast.LENGTH_SHORT).show();
                        // Access NewOrderFragment and refresh product list
//                        NewOrderFragment newOrderFragment = (NewOrderFragment) adapter.getFragment(2); // Assuming "New Orders" is the third tab (index 2)
//                        if (newOrderFragment != null) {
//                            newOrderFragment.refreshProductList();
//                        }
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


        TabLayoutMediator tabLayoutMediator =
                new TabLayoutMediator(tabLayout, vp2, new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position)
                        {
                            case 0:
                                tab.setText("Scheduled");
                                tab.setIcon(R.drawable.schedule_icon);
                                BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                                badgeDrawable.setNumber(count);
                                badgeDrawable.setMaxCharacterCount(2);
                                badgeDrawable.setVisible(true);
                                break;
                            case 1:{
                                tab.setText("Delivered");
                                tab.setIcon(R.drawable.delivered_icon);
                                BadgeDrawable badgeDrawable2 = tab.getOrCreateBadge();
                                badgeDrawable2.setNumber(delieveredCount);
                                badgeDrawable2.setMaxCharacterCount(2);
                                badgeDrawable2.setVisible(true);
                                break;}
                            default:
                                    tab.setText("New Orders");
                                    tab.setIcon(R.drawable.new_orders_icon);
//                                    BadgeDrawable badgeDrawable3 = tab.getOrCreateBadge();
//                                    badgeDrawable3.setNumber(newOrderCount);
//                                    badgeDrawable3.setMaxCharacterCount(2);
//                                    badgeDrawable3.setVisible(true);
                        }
                    }
                });
        tabLayoutMediator.attach();

        vp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                TabLayout.Tab selectedTab = tabLayout.getTabAt(position);
//                count++;
                BadgeDrawable badgeDrawable = selectedTab.getBadge();
                if(badgeDrawable != null)
                {
                    count=0;
                    badgeDrawable.setNumber(count);
                    if(!flag)
                        flag=true;
                    else
                        badgeDrawable.setVisible(false);
                }


//                   badgeDrawable.setNumber(count);


            }
        });

    }

    @Override
    public void productScheduled() {
        count++;
        updateBadge();
    }

    @Override
    public void delieveredProduct() {
        delieveredCount++;
        updateDeliveredBadge();
    }

//    @Override
//    public void newOrderProduct() {
//        newOrderCount++;
//        updateNewOrderBadge();
//    }

    public void updateBadge(){
        TabLayout.Tab scheduledTab = tabLayout.getTabAt(0);
        assert scheduledTab != null;
        BadgeDrawable badgeDrawable = scheduledTab.getOrCreateBadge();
        if (count > 0) {
            badgeDrawable.setNumber(count);
            badgeDrawable.setVisible(true);
        }
        else{
            badgeDrawable.setVisible(false);
        }
    }
    public void updateDeliveredBadge(){
        TabLayout.Tab scheduledTab = tabLayout.getTabAt(1);
        assert scheduledTab != null;
        BadgeDrawable badgeDrawable = scheduledTab.getOrCreateBadge();
        if (delieveredCount > 0) {
            badgeDrawable.setNumber(delieveredCount);
            badgeDrawable.setVisible(true);
        }
        else{
            badgeDrawable.setVisible(false);
        }
    }
    public void updateNewOrderBadge(){
        TabLayout.Tab scheduledTab = tabLayout.getTabAt(2);
        assert scheduledTab != null;
        BadgeDrawable badgeDrawable = scheduledTab.getOrCreateBadge();
        if (newOrderCount >= 0) {
            badgeDrawable.setNumber(newOrderCount);
            badgeDrawable.setVisible(true);
        }
        else{
            badgeDrawable.setVisible(false);
        }
    }

}