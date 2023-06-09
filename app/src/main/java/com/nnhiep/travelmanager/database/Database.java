package com.nnhiep.travelmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.nnhiep.travelmanager.R;
import com.nnhiep.travelmanager.models.Employee;
import com.nnhiep.travelmanager.models.Note;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Cơ sở dữ liệu của chương trình
 * @author nnhiep 18.03.2023
 */
public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TravelManager.db";
    private static final int DATABASE_VERSION = 2;
    private Context context;
    UserTable user;
    EmployeeTable employee;
    TourTable tour;
    NoteTable noteTable;
    SystemTable system;
    byte[] imageInBytes;

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        user = new UserTable();
        employee = new EmployeeTable();
        tour = new TourTable();
        noteTable = new NoteTable();
        system = new SystemTable();
        user.createTableUser(db);
        employee.createTableEmployee(db);
        tour.createTableTour(db);
        noteTable.createTableNote(db);
        system.createTableSystem(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS employee");
        db.execSQL("DROP TABLE IF EXISTS tour");
        db.execSQL("DROP TABLE IF EXISTS note");
        db.execSQL("DROP TABLE IF EXISTS system");
        onCreate(db);
    }

    public Cursor SelectData(String query) {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(query, null);
    }

    /**
     * Hàm truy vấn tự định nghĩa
     * @author nnhiep 18.03.2023
     */
    public Cursor customQuery(String query, Boolean isSelect) {
        SQLiteDatabase database;
        Cursor cursor = null;
        if(isSelect) {
            database = getReadableDatabase();
            database.execSQL(query);
        } else {
            database = getWritableDatabase();
            cursor = database.rawQuery(query, null);
        }
        database.close();
        return cursor;
    }


    // region Get
    /**
     * Lấy dữ liệu người dùng
     * @author nnhiep 18.03.2023
     */
    public Cursor getDataUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM user";

        Cursor cursor = null;

        if(db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    /**
     * Lấy dữ liệu nhân viên
     * @author nnhiep 18.03.2023
     */
    public List<Employee> getDataEmployee(String sortBy, String order) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Employee> data = new ArrayList<>();
        String query = "SELECT * FROM employee ORDER BY " + sortBy + " " + order;

        Cursor cursor = null;

        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        if (cursor != null) {
            while (cursor.moveToNext()){
                String id = cursor.getInt(0) + "";
                String name = cursor.getString(1);
                int age = cursor.getInt(2);
                int gender = cursor.getInt(3);
                String phone = cursor.getString(4);
                String gmail = cursor.getString(5);
                byte[] avatar = cursor.getBlob(6);
                Bitmap avatarBitmap = BitmapFactory.decodeByteArray(avatar, 0, avatar.length);
                Employee curEmployee = new Employee(id, name, phone, gmail, age, gender, avatarBitmap);
                data.add(curEmployee);
            }
            cursor.close();
        }

        return data;
    }

    /**
     * Lấy dữ liệu nhân viên theo bộ lọc và phân trang
     * @author nnhiep 18.03.2023
     */
    public List<Employee> getDataEmployeeByFilter(String searchValue) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Employee> data = new ArrayList<>();
        String query =
                "SELECT * FROM employee WHERE employee_name LIKE '%" + searchValue + "%' " +
                        "OR employee_phone LIKE '%" + searchValue + "%' ORDER BY employee_modified_date DESC";

        Cursor cursor = null;

        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        if (cursor != null) {
            while (cursor.moveToNext()){
                String id = cursor.getInt(0) + "";
                String name = cursor.getString(1);
                int age = cursor.getInt(2);
                int gender = cursor.getInt(3);
                String phone = cursor.getString(4);
                String gmail = cursor.getString(5);
                byte[] avatar = cursor.getBlob(6);
                Bitmap avatarBitmap = BitmapFactory.decodeByteArray(avatar, 0, avatar.length);
                Employee curEmployee = new Employee(id, name, phone, gmail, age, gender, avatarBitmap);
                data.add(curEmployee);
            }
            cursor.close();
        }

        return data;
    }

    /**
     * Lấy dữ liệu nhân viên theo ID
     * @author nnhiep 24.03.2023
     */
    public Employee getDataEmployeeByID(String targetId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Employee data = null;
        String query = "SELECT * FROM employee WHERE employee_id = \"" + targetId + "\"";

        Cursor cursor = null;

        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        if (cursor != null) {
            while (cursor.moveToFirst()){
                String id = cursor.getInt(0) + "";
                String name = cursor.getString(1);
                int age = cursor.getInt(2);
                int gender = cursor.getInt(3);
                String phone = cursor.getString(4);
                String gmail = cursor.getString(5);
                byte[] avatar = cursor.getBlob(6);
                Bitmap avatarBitmap = BitmapFactory.decodeByteArray(avatar, 0, avatar.length);
                data = new Employee(id, name, phone, gmail, age, gender, avatarBitmap);
                break;
            }
            cursor.close();
        }

        return data;
    }

    /**
     * Lấy dữ liệu hệ thống
     * @author nnhiep 18.03.2023
     */
    public Cursor getDataSystem() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT system_is_login FROM system";

        Cursor cursor = null;

        if(db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    /**
     * Lấy dữ liệu ghi chú
     * @author nnhiep 18.03.2023
     */
    public List<Note> getDataNote() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Note> data = new ArrayList<>();
        String query = "SELECT * FROM note";

        Cursor cursor = null;

        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        if (cursor != null){
            while (cursor.moveToNext()){
                Note note = new Note(cursor.getInt(0),
                        cursor.getString(1), cursor.getString(2));
                data.add(note);
            }
            cursor.close();
        }
        return data;
    }

    /**
     * Lấy dữ liệu tour
     * @author nnhiep 18.03.2023
     */
    public Cursor getDataFilterTour() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM tour";

        Cursor cursor = null;

        if(db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    // endregion


    // region Insert
    /**
     * Thêm mới một người dùng
     * @author nnhiep 18.03.2023
     */
    public void insertAnUser(String name, int age, int gender, String account, String pasword, String phone, byte[] avatar) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("user_name", name);
        cv.put("user_age", age);
        cv.put("user_gender", gender);
        cv.put("user_account", account);
        cv.put("user_password", pasword);
        cv.put("user_phone", phone);
        cv.put("user_avatar", avatar);

        long result =  db.insert("user", null, cv);

        if(result == -1) {
            Toast.makeText(context, context.getResources().getString(R.string.insert_failed), Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    /**
     * Thêm mới một nhân viên
     * @author nnhiep 18.03.2023
     */
    public void insertAnEmployee(String name, int age, int gender, String phone, String gmail, Bitmap avatar) {
        Date now = new Date();
        SQLiteDatabase db = this.getWritableDatabase();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        avatar.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        imageInBytes = byteArrayOutputStream.toByteArray();

        ContentValues cv = new ContentValues();

        cv.put("employee_name", name);
        cv.put("employee_age", age);
        cv.put("employee_gender", gender);
        cv.put("employee_phone", phone);
        cv.put("employee_gmail", gmail);
        cv.put("employee_avatar", imageInBytes);
        cv.put("employee_created_date", String.valueOf(now));
        cv.put("employee_modified_date", String.valueOf(now));

        long result =  db.insert("employee", null, cv);

        if(result == -1) {
            Toast.makeText(context, context.getResources().getString(R.string.insert_employee_failed), Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    /**
     * Thêm mới dữ liệu hệ thống
     * @author nnhiep 19.03.2023
     */
    public void insertDataSystem(boolean isLogin) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("system_is_login", isLogin);

        long result =  db.insert("system", null, cv);

        if(result == -1) {
            Toast.makeText(context, context.getResources().getString(R.string.insert_failed), Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    /**
     * Thêm mới một ghi chú
     * @author nnhiep 18.03.2023
     */
    public void insertANote(String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("note_title", title);
        cv.put("note_description", description);

        long result =  db.insert("note", null, cv);

        if(result == -1) {
            Toast.makeText(context, context.getResources().getString(R.string.insert_failed), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.insert_success), Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    /**
     * Thêm mới một tour
     * @author nnhiep 18.03.2023
     */
    public void insertATour(String title, double price, String start_date, String end_date, boolean isFavor, String createdBy, byte[] image) {
        Date now = new Date();
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("tour_title", title);
        cv.put("tour_image", image);
        cv.put("tour_price", price);
        cv.put("start_date", start_date);
        cv.put("end_date", end_date);
        cv.put("tour_isFavor", isFavor);
        cv.put("tour_created_date", String.valueOf(now));
        cv.put("tour_created_by", createdBy);
        cv.put("tour_modified_date", String.valueOf(now));
        cv.put("tour_modified_by", createdBy);

        long result =  db.insert("tour", null, cv);

        if(result == -1) {
            Toast.makeText(context, context.getResources().getString(R.string.insert_tour_failed), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.insert_tour_success), Toast.LENGTH_SHORT).show();
        }

        db.close();
    }
    // endregion


    // region Update
    /**
     * Sửa thông tin người dùng
     * @author nnhiep 18.03.2023
     */
    public void updateAnUser(String row_id, String name, int age, int gender, String account, String pasword, String phone, byte[] avatar) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("user_name", name);
        cv.put("user_age", age);
        cv.put("user_gender", gender);
        cv.put("user_account", account);
        cv.put("user_password", pasword);
        cv.put("user_phone", phone);
        cv.put("user_avatar", avatar);

        long result =  db.update("user", cv, "user_id=?", new String[]{row_id});

        if(result == -1) {
            Toast.makeText(context, context.getResources().getString(R.string.update_failed), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.update_success), Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    /**
     * Sửa thông tin nhân viên
     * @author nnhiep 18.03.2023
     */
    public void updateAnEmployee(String row_id, String name, int age, int gender, String phone, String gmail, Bitmap avatar) {
        SQLiteDatabase db = this.getWritableDatabase();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        avatar.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        imageInBytes = byteArrayOutputStream.toByteArray();

        ContentValues cv = new ContentValues();

        cv.put("employee_name", name);
        cv.put("employee_age", age);
        cv.put("employee_gender", gender);
        cv.put("employee_phone", phone);
        cv.put("employee_gmail", gmail);
        cv.put("employee_avatar", imageInBytes);
        cv.put("employee_modified_date", String.valueOf(new Date()));

        long result =  db.update("employee", cv, "employee_id=?", new String[]{row_id});

        if(result == -1) {
            Toast.makeText(context, context.getResources().getString(R.string.update_employee_failed), Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    /**
     * Sửa thông tin hệ thống
     * @author nnhiep 18.03.2023
     */
    public void updateDataSystem(String row_id, boolean isLogin) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("system_is_login", isLogin);

        long result =  db.update("system", cv, "system_id=?", new String[]{row_id});

        if(result == -1) {
            Toast.makeText(context, context.getResources().getString(R.string.update_failed), Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    /**
     * Sửa thông tin ghi chú
     * @author nnhiep 18.03.2023
     */
    public void updateANote(String title, String description, String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("note_title", title);
        cv.put("note_description", description);

        long result =  db.update("note", cv, "note_id=?", new String[]{row_id});

        if(result == -1) {
            Toast.makeText(context, context.getResources().getString(R.string.update_failed), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.update_success), Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    /**
     * Sửa thông tin tour
     * @author nnhiep 18.03.2023
     */
    public void updateATour(String row_id, String title, double price, String start_date, String end_date, boolean isFavor, String modifiedBy, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("tour_title", title);
        cv.put("tour_image", image);
        cv.put("tour_price", price);
        cv.put("start_date", start_date);
        cv.put("end_date", end_date);
        cv.put("tour_isFavor", isFavor);
        cv.put("tour_modified_date", String.valueOf(new Date()));
        cv.put("tour_modified_by", modifiedBy);

        long result =  db.update("tour", cv, "tour_id=?", new String[]{row_id});

        if(result == -1) {
            Toast.makeText(context, context.getResources().getString(R.string.update_tour_failed), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.update_tour_success), Toast.LENGTH_SHORT).show();
        }

        db.close();
    }
    // endregion


    // region Delete
    /**
     * Hàm xóa thông tin người dùng
     * @author nnhiep 18.03.2023
     */
    public void deleteUser(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result =  db.delete("user", "user_id=?", new String[]{row_id});

        if(result == -1) {
            Toast.makeText(context, context.getResources().getString(R.string.delete_failed), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    /**
     * Hàm xóa thông tin nhân viên
     * @author nnhiep 18.03.2023
     */
    public void deleteEmployee(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result =  db.delete("employee", "employee_id=?", new String[]{row_id});

        if(result == -1) {
            Toast.makeText(context, context.getResources().getString(R.string.delete_employee_failed), Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    /**
     * Hàm xóa một ghi chú
     * @author nnhiep 18.03.2023
     */
    public void deleteANote(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result =  db.delete("note", "note_id=?", new String[]{row_id});

        if(result == -1) {
            Toast.makeText(context, context.getResources().getString(R.string.delete_failed), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    /**
     * Hàm xóa một tour
     * @author nnhiep 18.03.2023
     */
    public void deleteATour(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result =  db.delete("tour", "tour_id=?", new String[]{row_id});

        if(result == -1) {
            Toast.makeText(context, context.getResources().getString(R.string.delete_tour_failed), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.delete_tour_success), Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    /**
     * Hàm xóa tất cả người dùng
     * @author nnhiep 18.03.2023
     */
    public void deleteAllUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM user");
    }

    /**
     * Hàm xóa tất cả nhân viên
     * @author nnhiep 18.03.2023
     */
    public void deleteAllEmployee() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM employee");
    }

    /**
     * Hàm xóa tất cả ghi chú
     * @author nnhiep 18.03.2023
     */
    public void deleteAllNote() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM note");
    }

    /**
     * Hàm xóa tất cả tour
     * @author nnhiep 18.03.2023
     */
    public void deleteAllTour() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM tour");
    }
    // endregion
}