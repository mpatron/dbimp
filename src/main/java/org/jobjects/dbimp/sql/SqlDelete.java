package org.jobjects.dbimp.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jobjects.dbimp.report.ReportTypeLine;
import org.jobjects.dbimp.trigger.Field;
import org.jobjects.dbimp.trigger.Line;
import org.jobjects.dbimp.xml.XmlField;


/**
 * Class permettant de supprimer une donnée dans la base. Créer le 24 janv.
 * 2003.
 * 
 * @author Mickaël Patron
 */
public class SqlDelete extends SqlStatement {

	private Logger log = Logger.getLogger(getClass().getName());

	private int count = 0;

	// ---------------------------------------------------------------------------

	public SqlDelete(Connection connection, String schemaName, boolean cached,
	    Line xmlline, ReportTypeLine reportTypeLine) throws SQLException {
		super(connection, schemaName, cached, xmlline, reportTypeLine);
	}

	// ---------------------------------------------------------------------------

	/**
	 * @see org.jobjects.dbimp.sql.SqlStatement#createSQL()
	 */
	public String createSQL() throws SQLException {
		String returnValue = "delete from " + getSQLSchemaName()
				+ getXmlline().getTableName();
		String where = "";

		boolean first = true;

		for (Iterator<Field> it = getXmlline().getFields().iterator(); it.hasNext();) {
			XmlField field = (XmlField) it.next();
			if (!field.isUse())
				continue;

			if (getPrimaries().contains(field.getName())) {
				if (first) {
					first = false;
					where += (field.getName() + "=?");
				} else {
					where += (" and " + field.getName() + "=?");
				}
			}
		}

		if (!where.equals("")) {
			returnValue += (" where " + where);
		}

		return returnValue;
	}

	// ---------------------------------------------------------------------------

	/**
	 * @see org.jobjects.dbimp.sql.SqlAction#execute(int nbLigne)
	 */
	public int execute(int nbLigne) {
		int returnValue = 0;
		int i = 1;
		boolean flag = true;
		try {
			if (getXmlline().getTrigger() != null) {

				flag = getXmlline().getTrigger().beforeDelete(getConnection(),
						nbLigne, getReportTypeLine().getReportTrigger(),
						getXmlline());
			}

			PreparedStatement pstmt = null;
			try {
				if (isCached()) {
					pstmt = getPstmtCached();
				} else {
					pstmt = getConnection().prepareStatement(getSql());
				}

				if (flag) {
					for (Iterator<Field> it = getXmlline().getFields().iterator(); it.hasNext();) {
						XmlField field = (XmlField) it.next();
						if (!field.isUse())
							continue;

						if (getPrimaries().contains(
								field.getName().toUpperCase())) {
							flag &= checkIn(getXmlline(), field,
									getReportTypeLine().getReportLine());

							if ((field.getBuffer() == null)
									|| field.getBuffer().equals("")) {
								setNull(pstmt, i, field);
							} else {
								setAll(pstmt, i, field);
							}
							i++;
							log.finest("(" + nbLigne
									+ ") delete where fieldname="
									+ field.getName() + " value="
									+ field.getBuffer());
						}
					}
				}

				if (flag) {
					returnValue = pstmt.executeUpdate();
					log.finest("Suppresion effectué : " + getSql());
					if (getXmlline().getTrigger() != null) {
						getXmlline().getTrigger().afterDelete(getConnection(),
								nbLigne,
								getReportTypeLine().getReportTrigger(),
								getXmlline());
					}
					/*
					 * Afin de ne pas mettre des informations qui ne sont pas
					 * des erreurs dans le fichier de rapport, il ne faut pas
					 * executer cette commande.
					 * getReportTypeLine().getReportLine().INFO_LINE_DESTROY();
					 */
				}
			} finally {
				if (!isCached()) {
					pstmt.close();
					pstmt = null;
				}
			}
		} catch (SQLException ex) {
			SqlUtils.AfficheSQLException(getXmlline(), getSql(), ex,
					getReportTypeLine().getReportLine());
		} catch (Exception throwable) {
			log.log(Level.SEVERE, "Erreur grave", throwable);
		}
		count += returnValue;
		getReportTypeLine().addToNbDelete(returnValue);
		return returnValue;
	}

	// ---------------------------------------------------------------------------

	/**
	 * @see org.jobjects.dbimp.sql.SqlAction#getCount()
	 */
	public int getCount() {
		return this.count;
	}

}
