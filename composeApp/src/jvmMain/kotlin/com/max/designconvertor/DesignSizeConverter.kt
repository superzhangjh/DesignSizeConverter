import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.set

@Composable
fun DesignSizeConverterApp(settings: PreferencesSettings) {
    // 从Settings加载保存的值
    var designBaseSize by remember { mutableStateOf(settings.getString("designBase", "")) }
    var actualBaseSize by remember { mutableStateOf(settings.getString("actualBase", "")) }
    var designValue by remember { mutableStateOf(settings.getString("designValue", "")) }
    var actualValue by remember { mutableStateOf(settings.getString("actualValue", "")) }

    // 保存数据到Settings
    fun saveData() {
        settings.apply {
            set("designBase", designBaseSize)
            set("actualBase", actualBaseSize)
            set("designValue", designValue)
            set("actualValue", actualValue)
        }
    }

    // 计算比例并自动更新值
    LaunchedEffect(designBaseSize, actualBaseSize, designValue, actualValue) {
        val designBase = designBaseSize.toFloatOrNull()
        val actualBase = actualBaseSize.toFloatOrNull()

        if (designBase != null && actualBase != null && actualBase != 0f) {
            val ratio = designBase / actualBase

            // 当基准尺寸变化时，重新计算转换值
            if (designValue.isNotEmpty()) {
                val dValue = designValue.toFloatOrNull()
                if (dValue != null) {
                    actualValue = (dValue / ratio).toString()
                }
            } else if (actualValue.isNotEmpty()) {
                val aValue = actualValue.toFloatOrNull()
                if (aValue != null) {
                    designValue = (aValue * ratio).toString()
                }
            }

            // 保存数据
            saveData()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "设计尺寸转换器",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // 基准尺寸部分
        Text(
            text = "基准尺寸",
            style = MaterialTheme.typography.titleLarge
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = designBaseSize,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                        designBaseSize = newValue
                    }
                },
                label = { Text("设计稿尺寸") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = actualBaseSize,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                        actualBaseSize = newValue
                    }
                },
                label = { Text("实际尺寸") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        // 转换值部分
        Text(
            text = "转换值",
            style = MaterialTheme.typography.titleLarge
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = designValue,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                        designValue = newValue
                        // 清空实际值以便自动计算
                        if (newValue.isNotEmpty()) {
                            actualValue = ""
                        }
                        saveData()
                    }
                },
                label = { Text("设计值") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = actualValue,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                        actualValue = newValue
                        // 清空设计值以便自动计算
                        if (newValue.isNotEmpty()) {
                            designValue = ""
                        }
                        saveData()
                    }
                },
                label = { Text("实际值") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }
    }
}