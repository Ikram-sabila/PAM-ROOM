package id.ac.ub.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ItemDao {
    @Query("SELECT * FROM item")
    List<Item> getAll();

    @Insert
    void insertAll(Item... items);

    @Delete
    void delete(Item item);

    @Update
    void update(Item item);

}
