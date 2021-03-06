package customer;

import static constants.MessageConstants.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import util.LogUtil;
import dao.CustomerDao;
import dao.CustomerDaoWithTransaction;


/**
 * 顧客管理（CSV）のロジック
 */
public class CustomerCsvLogic {

    /** テーブルへレコード追加 */
    private static final String DB_INSERT = "i";
    /** テーブルのレコード更新 */
    private static final String DB_UPDATE = "u";
    /** テーブルからレコード削除 */
    private static final String DB_DELETE = "d";

    /**
     * 一括処理のCSVファイルを読み込み、処理区分(i/u/d)に従って、DBへ顧客情報を追加/更新/削除する
     * <pre>
     * 一括処理が途中で失敗した場合は、途中までの一括処理をすべてロールバックする
     * </pre>
     * @param filePath 一括処理のCSVファイルのファイルパス
     * @return エラーメッセージ(処理成功時、null)
     */
    public String read(String filePath) {
        LogUtil.println(this.getClass().getSimpleName() + "#read");

        String errMessage = null;
        FileReader fReader = null;
        BufferedReader bReader = null;
        String line = null;

        CustomerDaoWithTransaction customerDao = new CustomerDaoWithTransaction();
        customerDao.startTransaction();

        try {
            fReader = new FileReader(filePath);
            bReader = new BufferedReader(fReader, 255);
            line = "";
            boolean isProcessed = false;

            while ((line = bReader.readLine()) != null) {
                LogUtil.println(line);
                StringTokenizer token = new StringTokenizer(line, ",");
                if (token.countTokens() == 0) {
                    continue;
                }
                String action = token.nextToken();

                switch(action) {
                case DB_INSERT:
                    LogUtil.println("INSERT");
                    if (token.countTokens() < 8) {
                        errMessage = MESSAGE_NOT_ENOUGH_DATA + "<br>" + line + "<br>";
                        customerDao.cancelTransaction();
                        return errMessage;
                    }

                    CustomerBean customer = new CustomerBean();

                    // IDは無視する
                    token.nextToken();

                    customer.setName(token.nextToken());
                    customer.setZip(token.nextToken());
                    customer.setAddress1(token.nextToken());
                    customer.setAddress2(token.nextToken());
                    customer.setTel(token.nextToken());
                    customer.setFax(token.nextToken());
                    customer.setEmail(token.nextToken());
                    errMessage = customerDao.add(customer);
                    if (errMessage != null) {
                        errMessage = MESSAGE_CAN_NOT_ADD + "<br>" + line + "<br>";
                        customerDao.cancelTransaction();
                        return errMessage;
                    }
                    isProcessed = true;
                    break;
                case DB_UPDATE:
                    LogUtil.println("UPDATE");
                    if (token.countTokens() < 8) {
                        errMessage = MESSAGE_NOT_ENOUGH_DATA + "<br>" + line + "<br>";
                        customerDao.cancelTransaction();
                        return errMessage;
                    }

                    customer = new CustomerBean();

                    try {
                        int intId = Integer.parseInt(token.nextToken());
                        customer.setId(intId);
                    } catch (NumberFormatException e) {
                        errMessage = MESSAGE_INVALID_ID + "<br>" + line + "<br>";
                        customerDao.cancelTransaction();
                        return errMessage;
                    }
                    customer.setName(token.nextToken());
                    customer.setZip(token.nextToken());
                    customer.setAddress1(token.nextToken());
                    customer.setAddress2(token.nextToken());
                    customer.setTel(token.nextToken());
                    customer.setFax(token.nextToken());
                    customer.setEmail(token.nextToken());
                    errMessage = customerDao.update(customer);
                    if (errMessage != null) {
                        errMessage = MESSAGE_NO_EXIST_DATA_TO_UPDATE + "<br>" + line + "<br>";
                        customerDao.cancelTransaction();
                        return errMessage;
                    }
                    isProcessed = true;
                    break;
                case DB_DELETE:
                    LogUtil.println("DELETE");
                    if (token.countTokens() < 1) {
                        errMessage = MESSAGE_NOT_ENOUGH_DATA + "<br>" + line + "<br>";
                        customerDao.cancelTransaction();
                        return errMessage;
                    }

                    int intId = 0;
                    try {
                        intId = Integer.parseInt(token.nextToken());
                    } catch (NumberFormatException e) {
                        errMessage = MESSAGE_INVALID_ID + "<br>" + line + "<br>";
                        customerDao.cancelTransaction();
                        return errMessage;
                    }

                    errMessage = customerDao.delete(intId);
                    if (errMessage != null) {
                        errMessage = MESSAGE_NO_EXIST_DATA_TO_DELETE + "<br>" + line + "<br>";
                        customerDao.cancelTransaction();
                        return errMessage;
                    }
                    isProcessed = true;
                    break;
                default:
                    LogUtil.println("処理なし。");
                    break;
                }
            }

            if (isProcessed) {
                customerDao.endTransaction();
            } else {
                errMessage = MESSAGE_DID_NOT_BATCH_PROCESSING;
                customerDao.cancelTransaction();
            }
        } catch (IOException e) {
            errMessage = MESSAGE_FAILED_BATCH_PROCESSING + "<br>" + e.getMessage() + "<br>";
            customerDao.cancelTransaction();
            LogUtil.printStackTrace(e);
        } finally {
            if (bReader != null) {
                try {
                    bReader.close();
                } catch (IOException e) {
                    errMessage = MESSAGE_FAILED_FILE_CLOSE + "<br>" + e.getMessage() + "<br>";
                    LogUtil.printStackTrace(e);
                }
            }
            if (fReader != null) {
                try {
                    fReader.close();
                } catch (IOException e) {
                    errMessage = MESSAGE_FAILED_FILE_CLOSE + "<br>" + e.getMessage() + "<br>";
                    LogUtil.printStackTrace(e);
                }
            }
        }

        return errMessage;
    }

    /**
     * 顧客情報の全レコードをDBから取得し、CSVファイルにエクスポートする
     * @param filePath エクスポートするCSVファイルのファイルパス
     * @return エラーメッセージ(処理成功時、null)
     */
    public String write(String filePath) {
        LogUtil.println(this.getClass().getSimpleName() + "#write");

        String errMessage = null;
        CustomerDao customerDao = new CustomerDao();
        ArrayList<CustomerBean> alCustomer = null;
        try {
            alCustomer = customerDao.loadAll();
        } catch (Exception e) {
            LogUtil.println(e.getMessage());
        }

        BufferedWriter bWriter = null;
        FileWriter fWriter = null;
        StringBuffer buffer = null;
        try {
            fWriter = new FileWriter(filePath, true);
            bWriter = new BufferedWriter(fWriter, 255);
            for(CustomerBean customer : alCustomer) {
                buffer = new StringBuffer();
                buffer.append(String.format(",%d,%s,%s,%s,%s,%s,%s,%s",
                        customer.getId(),
                        customer.getName(),
                        customer.getZip(),
                        customer.getAddress1(),
                        customer.getAddress2(),
                        customer.getTel(),
                        customer.getFax(),
                        customer.getEmail()));
                bWriter.write(buffer.toString() + System.getProperty("line.separator"));
                buffer = null;
            }
        } catch (IOException e) {
            errMessage = MESSAGE_FAILED_EXPORT + "<br>" + e.getMessage();
            LogUtil.printStackTrace(e);
        } finally {
            if (bWriter != null) {
                try {
                    bWriter.close();
                } catch (IOException e) {
                    errMessage = MESSAGE_FAILED_FILE_CLOSE + "<br>" + e.getMessage() + "<br>";
                    LogUtil.printStackTrace(e);
                }
            }
            if (fWriter != null) {
                try {
                    fWriter.close();
                } catch (IOException e) {
                    errMessage = MESSAGE_FAILED_FILE_CLOSE + "<br>" + e.getMessage() + "<br>";
                    LogUtil.printStackTrace(e);
                }
            }
        }

        return errMessage;
    }
}
