package com.example.inventoryappariellemoore;

import android.content.Context;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Handles all SMS notification logic.
 * This isolates platform-specific functionality and security checks
 * from the Activity.
 * Also features clean failure handling.
 */
public class SmsNotificationService {

    private static final String ALERT_PHONE_NUMBER = "5551234567"; // Placeholder for now

    private final Context context;
    private final DBHelper dbHelper;

    public SmsNotificationService(Context context) {
        this.context = context;
        this.dbHelper = new DBHelper(context);
    }

    // Sends an out-of-stock alert if user preferences and permissions allow it
    public void sendOutOfStockAlert(String username, String itemName) {

        // Check user preference stored in database
        if (!dbHelper.isSmsAllowed(username)) {
            Toast.makeText(context,
                    "SMS alerts are disabled for this user.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Checks runtime permission
        if (!PermissionManager.hasSmsPermission(context)) {
            Toast.makeText(context,
                    "SMS permission denied. Cannot send alert.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(
                    ALERT_PHONE_NUMBER,
                    null,
                    "Alert: '" + itemName + "' is now OUT OF STOCK.",
                    null,
                    null
            );

            Toast.makeText(context,
                    "SMS alert sent.",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(context,
                    "Failed to send SMS: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
}
