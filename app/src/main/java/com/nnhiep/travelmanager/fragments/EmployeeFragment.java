package com.nnhiep.travelmanager.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.nnhiep.travelmanager.R;
import com.nnhiep.travelmanager.adapters.EmployeeAdapter;
import com.nnhiep.travelmanager.database.Database;
import com.nnhiep.travelmanager.models.Employee;
import com.nnhiep.travelmanager.views.AddEmployeeActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * Employee - Hiệp
 */
public class EmployeeFragment extends Fragment {
    RecyclerView rViewEmployee;
    Database db;
    EmployeeAdapter adapter;
    List<Employee> dataSource;
    Button btnChangeView;
    TextView txtNoData;
    LinearLayout layoutItemEmployee;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee, container, false);
        View childView = inflater.inflate(R.layout.employee_item, container, false);
        layoutItemEmployee = childView.findViewById(R.id.layoutItemEmployee);
        db = new Database(this.getContext());

        rViewEmployee = view.findViewById(R.id.rViewEmployee);
        btnChangeView = view.findViewById(R.id.btnGridView);
        txtNoData = view.findViewById(R.id.txtNoData);

        getDataEmployee("employee_modified_date", "DESC");
        buildRecyclerView(view);
        buildDropdownSort(view);

        // Xử lý click thêm - nnhiep 20.03.2023
        view.findViewById(R.id.btnAddEmployee).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddEmployeeActivity.class);
            startActivityForResult(intent, 250);
        });

        // Xử lý đổi dạng layout - nnhiep 20.03.2023
        btnChangeView.setOnClickListener(v -> {
            // Chuyển về dạng grid view - GridLayout
            if(btnChangeView.getText().equals("Grid")) {
                rViewEmployee.setLayoutManager(new GridLayoutManager(getContext(), 3));
                layoutItemEmployee.setOrientation(LinearLayout.VERTICAL);

                btnChangeView.setText("List");
                btnChangeView.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.ic_baseline_view_list_24), null, null, null);
            } else {
                // Chuyển về dạng list view - LinearLayout
                rViewEmployee.setLayoutManager(new LinearLayoutManager(getContext()));
                layoutItemEmployee.setOrientation(LinearLayout.HORIZONTAL);

                btnChangeView.setText("Grid");
                btnChangeView.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.ic_baseline_grid_view_24), null, null, null);
            }
        });

        // Xử lý sự kiện tìm kiếm nhân viên - nnhiep 20.03.2023
        EditText eTxtSearchEmployee = view.findViewById(R.id.eTxtSearchEmployee);
        eTxtSearchEmployee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().isEmpty()) {

                } else filterEmployee(s.toString());
            }
        });

        return view;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        String phoneNumber = adapter.getItem(item.getGroupId()).getPhone();
        switch (item.getItemId()) {
            // Xử lý click gửi tin nhắn - nnhiep 22.03.2023
            case 1:
                Intent intentMessage = new Intent(Intent.ACTION_SENDTO);
                intentMessage.setData(Uri.parse("smsto:" + phoneNumber));
                startActivity(intentMessage);
                break;
            // Xử lý click gọi điện - nnhiep 22.03.2023
            case 2:
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                intentCall.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intentCall);
                break;
            // Xử lý click sửa - nnhiep 22.03.2023
            case 3:
                Intent intent = new Intent(this.getContext(), AddEmployeeActivity.class);
                intent.putExtra("ID", adapter.getItem(item.getGroupId()).getId());
                startActivityForResult(intent, 200);
                break;
            // Xử lý click xóa - nnhiep 22.03.2023
            case 4:
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("Delete");
                dialog.setMessage("Are you sure to delete employee " + adapter.getItem(item.getGroupId()).getName() + "?");
                dialog.setPositiveButton("Delete", (dialog1, which) -> {
                    db.deleteEmployee(adapter.getItem(item.getGroupId()).getId());
                    refreshData("employee_modified_date","DESC");
                });
                dialog.setNegativeButton("Cancel", (dialog2, which) -> {
                });
                dialog.show();
        }
        return super.onContextItemSelected(item);
    }

    /**
     * Hàm lấy dữ liệu nhân viên
     * @author nnhiep 30.03.2023
     */
    private void getDataEmployee(String sortBy, String order) {
//        dataSource.add(new Employee("1", "Nguyễn Ngọc Hiệp", "0972844478", "nnhiep582k2@gmail.com", 21, 1, R.drawable.employee_male));
//        dataSource.add(new Employee("2", "Nguyễn Thị Hoa", "0999999999", "nthoa@gmail.com", 45, 0, R.drawable.employee_female));
//        dataSource.add(new Employee("3", "Nguyễn Ngọc Sơn", "0988888888", "nnson@gmail.com", 18, 1, R.drawable.employee_male));
//        dataSource.add(new Employee("4", "Nguyễn Ngọc Điệp", "0977777777", "nvdiepgmail@gmail.com", 45, 1, R.drawable.employee_male));
//        dataSource.add(new Employee("5", "Ứng Phương Thảo", "0966666666", "upthao@gmail.com", 22, 0, R.drawable.employee_female));
//        dataSource.add(new Employee("6", "Lê Ngọc Lâm", "0955555555", "lnlam@gmail.com", 27, 2, R.drawable.employee_male));
//        dataSource.add(new Employee("7", "Nguyễn Quốc Đạt", "0922222222", "nqdat@gmail.com", 26, 1, R.drawable.employee_male));
//        dataSource.add(new Employee("8", "Tạ Long Khánh", "0933333333", "tlkhanh@gmail.com", 26, 2, R.drawable.employee_male));
//        dataSource.add(new Employee("9", "Phùng Đình Dương", "0922222222", "pdduong@gmail.com", 25, 2, R.drawable.employee_male));
        dataSource = db.getDataEmployee(sortBy, order);
        if(dataSource.size() == 0) {
            dataSource = new ArrayList<Employee>();
            rViewEmployee.setVisibility(View.GONE);
            txtNoData.setVisibility(View.VISIBLE);
        } else {
            txtNoData.setVisibility(View.GONE);
            rViewEmployee.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Hàm thiết lập recycle view
     * @author nnhiep 30.03.2023
     */
    private void buildRecyclerView(View view) {
        adapter = new EmployeeAdapter(dataSource);
        rViewEmployee.setLayoutManager(new LinearLayoutManager(getContext()));
        rViewEmployee.setAdapter(adapter);
    }

    /**
     * Hàm thiết lập dropdown cho chức năng sort
     * @author nnhiep 30.03.2023
     */
    private void buildDropdownSort(View view) {
        String[] dropdownSort = view.getResources().getStringArray(R.array.sort_employee);
        AutoCompleteTextView aCompleteSort =  view.findViewById(R.id.aCompleteSort);
        ArrayAdapter sortAdapter = new ArrayAdapter(requireContext(), R.layout.dropdown_employee_item, dropdownSort);
        aCompleteSort.setAdapter(sortAdapter);
        aCompleteSort.setOnItemClickListener((parent, view1, position, id) -> {
            switch (position) {
                case 0:
                    refreshData("employee_modified_date","DESC");
                    break;
                case 1:
                    refreshData("employee_name", "ASC");
                    break;
                case 2:
                    refreshData("employee_age", "ASC");
                    break;
                case 3:
                    refreshData("employee_gender", "ASC");
                    break;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200 && resultCode == 150) {
            refreshData("employee_modified_date", "DESC");
        }
        if(requestCode == 250 && resultCode == 150) {
            refreshData("employee_modified_date", "DESC");
        }
    }

    /**
     * Lấy lại dữ liệu
     * @author nnhiep 24.03.2023
     */
    private void refreshData(String sortBy, String order) {
        getDataEmployee(sortBy, order);
        adapter = new EmployeeAdapter(dataSource);
        rViewEmployee.setLayoutManager(new LinearLayoutManager(getContext()));
        rViewEmployee.setAdapter(adapter);
    }

    /**
     * Hàm lọc dữ liệu tìm kiếm
     * @author nnhiep 30.03.2023
     */
    private void filterEmployee(String searchValue) {
        ArrayList<Employee> filteredList = new ArrayList<>();
        ArrayList<Employee> temp = new ArrayList<Employee>(dataSource);
        for(int i = 0; i < temp.size(); i++) {
            if(temp.get(i).getName().toLowerCase().contains(searchValue)) {
                filteredList.add(temp.get(i));
            }
        }

        adapter.filter(filteredList);
    }
}