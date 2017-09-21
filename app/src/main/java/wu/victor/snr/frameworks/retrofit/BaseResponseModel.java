package wu.victor.snr.frameworks.retrofit;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wuguonan on 2017/2/28 0028.
 */

public class BaseResponseModel<T> implements Serializable {


  /**
   * reason : 成功的返回
   * result : {"stat":"1","data":[...]}
   * error_code : 0
   */

  private String reason;
  private int error_code;
  private ResultBean<T> result;


  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public int getError_code() {
    return error_code;
  }

  public void setError_code(int error_code) {
    this.error_code = error_code;
  }

  public ResultBean getResult() {
    return result;
  }

  public void setResult(ResultBean result) {
    this.result = result;
  }

  public void setData(List<T> data) {
    result = new ResultBean();
    result.data = data;
  }

  public class ResultBean<D> implements Serializable {
    private String stat;
    private List<D> data;


    public String getStat() {
      return stat;
    }

    public void setStat(String stat) {
      this.stat = stat;
    }

    public List<D> getData() {
      return data;
    }

    public void setData(List<D> data) {
      this.data = data;
    }
  }
}
