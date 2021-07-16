package life.alissonescorcio.businesscard

import android.app.Application
import life.alissonescorcio.businesscard.data.AppDataBase
import life.alissonescorcio.businesscard.data.BusinessCardRepository

class App : Application() {

    val database by lazy { AppDataBase.getDatabase(this)}
    val repository by lazy { BusinessCardRepository(database.businessDao()) }

}