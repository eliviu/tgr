import com.trg.assesment.Main

import scala.concurrent.Future
import javax.inject._
import play.api.inject.ApplicationLifecycle

@Singleton
class ApplicationStart @Inject() (lifecycle: ApplicationLifecycle) {

  println("start spark source data loading..")
  Main.loadSourceData()
  println("data loading completed..")
}