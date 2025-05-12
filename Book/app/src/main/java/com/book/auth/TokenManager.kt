package com.code.book.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import android.util.Log
import org.json.JSONObject

class TokenManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    companion object {
        private const val PREFS_NAME = "BookAuraPrefs"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_USER_ROLE = "user_role"
        private const val TAG = "TokenManager"
    }

    fun saveToken(token: String) {
        sharedPreferences.edit().putString(KEY_ACCESS_TOKEN, token).apply()
        // Log the full token
        Log.d(TAG, "Received token: $token")
        
        // Extract and save role from token
        extractRoleFromToken(token)?.let { role ->
            Log.d(TAG, "Extracted role from token: $role")
            saveUserRole(role)
        }
    }

    fun getToken(): String? {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
    }

    fun clearToken() {
        sharedPreferences.edit().remove(KEY_ACCESS_TOKEN).remove(KEY_USER_ROLE).apply()
    }

    fun isLoggedIn(): Boolean {
        return getToken() != null
    }

    fun saveUserRole(role: String) {
        Log.d(TAG, "Saving user role: $role")
        sharedPreferences.edit().putString(KEY_USER_ROLE, role).apply()
    }

    fun getUserRole(): String? {
        val role = sharedPreferences.getString(KEY_USER_ROLE, null)
        Log.d(TAG, "Getting user role: $role")
        return role
    }

    fun isSuperAdmin(): Boolean {
        val role = getUserRole()
        val isSuperAdmin = role == "super_admin" || role == "superadmin"
        Log.d(TAG, "Checking isSuperAdmin: role=$role, isSuperAdmin=$isSuperAdmin")
        return isSuperAdmin
    }

    fun getUserEmail(): String? {
        val token = getToken() ?: return null
        return try {
            extractEmailFromToken(token)
        } catch (e: Exception) {
            Log.e(TAG, "Error extracting email from token", e)
            null
        }
    }

    private fun extractRoleFromToken(token: String): String? {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) {
                Log.e(TAG, "Invalid token format: expected 3 parts")
                return null
            }

            val payload = parts[1]
            val decodedPayload = String(Base64.decode(payload, Base64.URL_SAFE))
            Log.d(TAG, "Decoded JWT payload: $decodedPayload")
            
            val jsonObject = JSONObject(decodedPayload)
            
            // Try different possible role field names
            val role = jsonObject.optString("role") ?: 
                      jsonObject.optString("userRole") ?: 
                      jsonObject.optString("user_role")
            
            if (role.isNotEmpty()) {
                Log.d(TAG, "Found role in token: $role")
            } else {
                Log.e(TAG, "No role found in token payload")
            }
            
            role
        } catch (e: Exception) {
            Log.e(TAG, "Error decoding token: ${e.message}")
            null
        }
    }

    private fun extractEmailFromToken(token: String): String? {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) {
                Log.e(TAG, "Invalid token format: expected 3 parts")
                return null
            }

            val payload = parts[1]
            val decodedPayload = String(Base64.decode(payload, Base64.URL_SAFE))
            Log.d(TAG, "Decoded JWT payload: $decodedPayload")
            
            val jsonObject = JSONObject(decodedPayload)
            
            // Try different possible email field names
            val email = jsonObject.optString("email") ?: 
                       jsonObject.optString("userEmail") ?: 
                       jsonObject.optString("user_email")
            
            if (email.isNotEmpty()) {
                Log.d(TAG, "Found email in token: $email")
            } else {
                Log.e(TAG, "No email found in token payload")
            }
            
            email
        } catch (e: Exception) {
            Log.e(TAG, "Error decoding token: ${e.message}")
            null
        }
    }
} 