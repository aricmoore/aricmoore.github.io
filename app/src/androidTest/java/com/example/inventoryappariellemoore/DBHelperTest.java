package com.example.inventoryappariellemoore;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Tests for DBHelper.java (Enhancement 3).
 * Verifies database CRUD operations, enhanced queries, and transactional integrity.
 */
@RunWith(AndroidJUnit4.class)
public class DBHelperTest {

    private DBHelper dbHelper;
    private long testUserId;

    @Before
    public void setUp() {
        // Arrange
        Context context = ApplicationProvider.getApplicationContext();
        dbHelper = new DBHelper(context);

        // Ensures a clean slate to begin
        dbHelper.getWritableDatabase().execSQL("DELETE FROM " + "items");
        dbHelper.getWritableDatabase().execSQL("DELETE FROM " + "users");

        // Arrange: create test user
        dbHelper.createUser("testuser", "password123");
        testUserId = dbHelper.getUserId("testuser");
        assertTrue("Test user should be created", testUserId != -1);
    }

    @After
    public void tearDown() {
        // Closes DBHelper to release resources
        dbHelper.close();
    }

    // User-related tests
    @Test
    public void testSmsPermissionToggle() {
        // Act
        dbHelper.setSmsAllowedForUser("testuser", true);

        // Assert
        assertTrue(dbHelper.isSmsAllowed("testuser"));

        // Act
        dbHelper.setSmsAllowedForUser("testuser", false);

        // Assert
        assertFalse(dbHelper.isSmsAllowed("testuser"));
    }

    // Item CRUD tests
    @Test
    public void testInsertAndRetrieveItem() {
        // Act
        long itemId = dbHelper.insertItem(testUserId, "Widget", 10);

        // Assert
        assertTrue("Item should be inserted", itemId != -1);

        // Act
        List<InventoryItem> items = dbHelper.getItemsForUser(testUserId);

        // Assert
        assertEquals("There should be one item for the test user", 1, items.size());

        // Act
        InventoryItem item = items.get(0);

        // Assert
        assertEquals("Item name should match", "Widget", item.name);
        assertEquals("Item quantity should match", 10, item.quantity);
    }

    @Test
    public void testUpdateItem() {
        // Arrange
        long itemId = dbHelper.insertItem(testUserId, "Cannoli", 5);

        // Act
        int rowsAffected = dbHelper.updateItem(itemId, "Cannoli Pro", 8);

        // Assert
        assertEquals("One row should be updated", 1, rowsAffected);

        // Act
        List<InventoryItem> items = dbHelper.getItemsForUser(testUserId);
        InventoryItem updated = items.get(0);

        // Assert
        assertEquals("Updated name should match", "Cannoli Pro", updated.name);
        assertEquals("Updated quantity should match", 8, updated.quantity);
    }

    @Test
    public void testDeleteItem() {
        // Arrange
        long itemId = dbHelper.insertItem(testUserId, "DeleteMe", 1);

        // Act
        int deleted = dbHelper.deleteItem(itemId);

        // Assert
        assertEquals("One row should be deleted", 1, deleted);

        // Act
        List<InventoryItem> items = dbHelper.getItemsForUser(testUserId);

        // Assert
        assertEquals("No items should remain", 0, items.size());
    }

    // Enhanced database method tests
    @Test
    public void testGetItemsByQuantityRange() {
        // Arrange
        dbHelper.insertItem(testUserId, "Item1", 5);
        dbHelper.insertItem(testUserId, "Item2", 10);
        dbHelper.insertItem(testUserId, "Item3", 15);

        // Act
        List<InventoryItem> filtered = dbHelper.getItemsByQuantityRange(testUserId, 6, 12);

        // Assert
        assertEquals("Only Item2 should be in range 6-12", 1, filtered.size());
        assertEquals("Filtered item name should match", "Item2", filtered.get(0).name);
    }

    @Test
    public void testSearchItemsByName() {
        // Arrange
        dbHelper.insertItem(testUserId, "Alpha", 1);
        dbHelper.insertItem(testUserId, "Beta", 2);
        dbHelper.insertItem(testUserId, "Alphanumeric", 3);

        // Act
        List<InventoryItem> results = dbHelper.searchItemsByName(testUserId, "Al");

        // Assert
        assertEquals("Should find 2 items containing 'Al'", 2, results.size());
    }

    @Test
    public void testDeleteUserCascadesItems() {
        // Arrange
        long itemId1 = dbHelper.insertItem(testUserId, "Cascade1", 1);
        long itemId2 = dbHelper.insertItem(testUserId, "Cascade2", 2);

        // Act
        dbHelper.deleteUser(testUserId);

        // Assert
        assertEquals("User should be gone", -1, dbHelper.getUserId("testuser"));

        // Act
        List<InventoryItem> items = dbHelper.getItemsForUser(testUserId);

        // Assert
        assertEquals("All items for deleted user should be removed", 0, items.size());
    }

    @Test
    public void testBatchUpdateQuantities() {
        // Arrange
        long id1 = dbHelper.insertItem(testUserId, "Batch1", 1);
        long id2 = dbHelper.insertItem(testUserId, "Batch2", 2);

        Map<Long, Integer> updates = new HashMap<>();
        updates.put(id1, 10);
        updates.put(id2, 20);

        // Act
        dbHelper.batchUpdateQuantities(updates);

        // Act (retrieves items)
        List<InventoryItem> items = dbHelper.getItemsForUser(testUserId);

        // Assert
        for (InventoryItem i : items) {
            if (i.name.equals("Batch1")) assertEquals(10, i.quantity);
            if (i.name.equals("Batch2")) assertEquals(20, i.quantity);
        }
    }
}
