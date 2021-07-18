package life.alissonescorcio.businesscard.ui


import androidx.activity.viewModels
import life.alissonescorcio.businesscard.App
import life.alissonescorcio.businesscard.R
import life.alissonescorcio.businesscard.data.BusinessCard
import life.alissonescorcio.businesscard.databinding.ActivityAddBusinessCardBinding
//import top.defaults.colorpicker.ColorPickerPopup
//import top.defaults.colorpicker.ColorPickerPopup.ColorPickerObserver
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import vadiole.colorpicker.ColorModel
import vadiole.colorpicker.ColorPickerDialog

class AddBusinessCardActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityAddBusinessCardBinding.inflate(layoutInflater)
    }

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as App).repository)
    }

    private var mDefaultColor: Int = 0

    private var codigoCor: Int = 0

    private val colorKey = "KEY_COLOR"

    @ColorInt
    var currentColor: Int = Color.LTGRAY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // set the default color to 0 as it is black
        mDefaultColor = 0
        codigoCor = 0
        insertListeners()

        //binding.previewSelectedColor.setBackgroundColor(currentColor)

        //val colorView = binding.previewSelectedColor
        val pickColor = binding.btColorPicker
        //val useAlpha = findViewById<CheckBox>(R.id.checkbox_use_alpha)
        //val colorModelSwitchEnabled = findViewById<CheckBox>(R.id.chackbox_enabled_switch)


        //  restore color and listeners after activity recreate
        if (savedInstanceState != null) {
            currentColor = savedInstanceState.getInt(colorKey)

            val colorPicker =
                supportFragmentManager.findFragmentByTag("color_picker") as ColorPickerDialog?
            colorPicker?.setOnSelectColorListener { color ->
                //  save color to variable
                currentColor = color

                //  set background color to view result
                //colorView.setBackgroundColor(color)
                binding.mcvContent.setCardBackgroundColor(color)
            }

            colorPicker?.setOnSwitchColorModelListener { colorModel ->
                Toast.makeText(this, "Switched to ${colorModel.name}", Toast.LENGTH_SHORT).show()
            }
        }


        //  set current color as background
        //colorView.setBackgroundColor(currentColor)
        binding.mcvContent.setCardBackgroundColor(currentColor)
        //  when button click -> pick color
        pickColor.setOnClickListener {

            //  Create Builder
            val colorPicker: ColorPickerDialog = ColorPickerDialog.Builder()
                //  set initial (default) color
                .setInitialColor(currentColor)

                //  set Color Model. If use alpha - ARGB, else RGB. Use what your want
                .setColorModel(ColorModel.RGB)

                //  set is user be able to switch color model. If ARGB - switch not available
                .setColorModelSwitchEnabled(true)

                //  set your localized string resource for OK button
                .setButtonOkText(R.string.action_ok)

                //  set your localized string resource for Cancel button
                .setButtonCancelText(R.string.action_cancel)

                //  callback for switched color model
                .onColorModelSwitched { colorModel ->
                    Toast.makeText(this, "Switched to ${colorModel.name}", Toast.LENGTH_SHORT)
                        .show()
                }

                //  callback for picked color (required)
                .onColorSelected { color: Int ->

                    //  save color to variable
                    currentColor = color

                    //  set background color to view result
                    //colorView.setBackgroundColor(color)
                    binding.mcvContent.setCardBackgroundColor(color)
                }

                //  create dialog
                .create()


            //  show color picker with supportFragmentManager (or childFragmentManager in Fragment)
            colorPicker.show(supportFragmentManager, "color_picker")
        }
    }

    private fun insertListeners(){
        binding.btClose.setOnClickListener {
            finish()
        }

        binding.btConfirmar.setOnClickListener {
            val businessCard = BusinessCard(
                nome = binding.tilName.editText?.text.toString(),
                empresa = binding.tilEmpresa.editText?.text.toString(),
                telefone = binding.tilTelefone.editText?.text.toString(),
                email = binding.tilEmail.editText?.text.toString(),
                fundoPersonalizado = currentColor.toString()
            )

            mainViewModel.insert(businessCard)
            Toast.makeText(this, R.string.label_show_success, Toast.LENGTH_SHORT ).show()

            finish()
        }

        binding.etName?.doOnTextChanged { text, _, _, _ ->
            binding.tvNome?.text = text
        }

        binding.etTelefone?.doOnTextChanged { text, _, _, _ ->
            binding.tvTelefone?.text = text
        }

        binding.etEmail?.doOnTextChanged { text, _, _, _ ->
            binding.tvEmail?.text = text
        }

        binding.etEmpresa?.doOnTextChanged { text, _, _, _ ->
            binding.tvNomeEmpresa?.text = text
        }

    }
}