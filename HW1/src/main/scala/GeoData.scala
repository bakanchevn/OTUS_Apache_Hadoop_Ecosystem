import java.io.PrintWriter
import scala.io.Source
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._
import org.json4s.native.Document



object GeoData  {

  /**
   * Загрузите файл с географическими данными различных стран (https://raw.githubusercontent.com/mledoze/countries/master/countries.json)
   * Среди стран выберите 10 стран Африки с наибольшей площадью
   * Запишите данные о выбранных странах в виде JSON-массива объектов следующей структуры:
   * [{
   * “name”: <Официальное название страны на английском языке, строка>,
   * “capital”: <Название столицы, строка>(если столиц перечисленно несколько, выберите первую),
   * “area”: <Площадь страны в квадратных километрах, число>,
   * }]
   * Обеспечьте проект инструкциями для сборки JAR-файла, принимающего на вход имя выходного файла и осуществляющего запись в него
   *
   * Справка:
   * Для парсинга JSON легче всего использовать библиотеки Json4s, Spray-json, Play-json или Circe.
   * Пример парсинга данных JSON в Scala-объекты – https://github.com/json4s/json4s#extracting-values
   *
   */

  def source: Source = Source.fromURL(
    "https://raw.githubusercontent.com/mledoze/countries/master/countries.json"
  )

  case class NameCountry(official: String)
  case class GeoCountryData(area: Option[Int], name: NameCountry,  capital: List[String], region: String)

  def generateJson(geoCountryData: GeoCountryData): Document = {
    val json =
      ("name" -> geoCountryData.name.official) ~
        ("capital" -> geoCountryData.capital.headOption) ~
        ( "area" -> geoCountryData.area)

    render(json)
  }


  def writeToFile(fileName: String, geoCountryDataList: List[GeoCountryData]): Unit = {
    val writer = new PrintWriter(fileName)
    geoCountryDataList.foreach(x => writer.println(compact(generateJson(x))))
    writer.flush()
    writer.close()
  }

 def main(args: Array[String]): Unit = {
    if (args.length != 1)
      throw new RuntimeException(s"The args count ${args.length} is not correct")
    val fileName = args(0)
    implicit lazy val formats: Formats = DefaultFormats
    val geoData = parse(source.mkString)
      .extract[List[GeoCountryData]]
      .filter(_.region == "Africa")
      .sortBy(_.area.getOrElse(0))(Ordering[Int].reverse)
      .take(10)
    writeToFile(fileName, geoData)
  }


}
