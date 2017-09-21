package wu.victor.snr.business.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

import wu.victor.snr.frameworks.database.store.SnrDataBase;

/**
 * Created by wuguonan on 2017/3/16 0016.
 */
@ModelContainer
@Table(database = SnrDataBase.class)
public class NewsFavorites extends BaseModel implements Serializable {
  @PrimaryKey
  private String uniquekey;
  @Column
  private String title;
  @Column
  private String date;
  @Column
  private String category;
  @Column
  private String author_name;
  @Column
  private String url;
  @Column
  private String thumbnail_pic_s;
  @Column
  private String thumbnail_pic_s02;
  @Column
  private String thumbnail_pic_s03;
  @Column
  private String type;

  public String getUniquekey() {
    return uniquekey;
  }

  public void setUniquekey(String uniquekey) {
    this.uniquekey = uniquekey;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getAuthor_name() {
    return author_name;
  }

  public void setAuthor_name(String author_name) {
    this.author_name = author_name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getThumbnail_pic_s() {
    return thumbnail_pic_s;
  }

  public void setThumbnail_pic_s(String thumbnail_pic_s) {
    this.thumbnail_pic_s = thumbnail_pic_s;
  }

  public String getThumbnail_pic_s02() {
    return thumbnail_pic_s02;
  }

  public void setThumbnail_pic_s02(String thumbnail_pic_s02) {
    this.thumbnail_pic_s02 = thumbnail_pic_s02;
  }

  public String getThumbnail_pic_s03() {
    return thumbnail_pic_s03;
  }

  public void setThumbnail_pic_s03(String thumbnail_pic_s03) {
    this.thumbnail_pic_s03 = thumbnail_pic_s03;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

}
