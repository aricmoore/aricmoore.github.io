package com.example.inventoryappariellemoore;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

/**
 * Centralises runtime permission checks.
 * This avoids duplicated logic and clarifies security-related responsibilities.
 * Also removes magic permission logic from Activity.
 */
public class PermissionManager {

    private PermissionManager() {
        // Utility class – no instances
    }

    public static boolean hasSmsPermission(Context context) {
        return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED;
    }
}
