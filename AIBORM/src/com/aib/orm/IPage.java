package com.aib.orm;

import com.aib.orm.dbobject.ForeignKeyViolationException;
import java.sql.SQLException;

/**
 *
 * @author Nick Mukhin
 */
public interface IPage {
    Object getPagescan();
    void setPagescan(Object pagescan) throws SQLException, ForeignKeyViolationException;
    String getFileextension();
    void setFileextension(String fileextension) throws SQLException, ForeignKeyViolationException;
}
