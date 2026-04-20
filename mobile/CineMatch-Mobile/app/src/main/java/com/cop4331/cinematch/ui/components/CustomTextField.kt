package com.cop4331.cinematch.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.cop4331.cinematch.ui.theme.*

@Composable
fun CustomTextField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column {
        // 🔹 LABEL ABOVE FIELD
        Text(
            text = label.uppercase(),
            color = LightGray,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        TextField(
            value = value,
            onValueChange = onChange,
            placeholder = { Text(placeholder) },
            singleLine = true,
            visualTransformation = if (isPassword && !passwordVisible)
                PasswordVisualTransformation()
            else VisualTransformation.None,
            trailingIcon = {
                if (isPassword) {
                    IconButton(onClick = {
                        passwordVisible = !passwordVisible
                    }) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Visibility,
                            contentDescription = null
                        )
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF1C1C1C),
                unfocusedContainerColor = Color(0xFF1C1C1C),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier.fillMaxWidth()
        )
    }
}