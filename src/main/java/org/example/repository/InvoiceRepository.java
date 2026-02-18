package org.example.repository;

import org.example.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class InvoiceRepository {
    private final Connection connection;

    public InvoiceRepository(Connection connection) {
        this.connection = connection;
    }

    // --- Q1 : Total par facture ---
    public List<InvoiceTotal> findInvoiceTotals() {
        List<InvoiceTotal> totals = new ArrayList<>();
        String sql = """
            SELECT i.id, i.customer_name, i.status, 
                   SUM(l.quantity * l.unit_price) as total_amount
            FROM invoice i
            JOIN invoice_line l ON i.id = l.invoice_id
            GROUP BY i.id, i.customer_name, i.status
            ORDER BY i.id;
        """;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                totals.add(new InvoiceTotal(
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        rs.getString("status"),
                        rs.getBigDecimal("total_amount")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totals;
    }

    // --- Q2 : Total factures CONFIRMED et PAID ---
    public List<InvoiceTotal> findConfirmedAndPaidInvoiceTotals() {
        List<InvoiceTotal> totals = new ArrayList<>();
        String sql = """
            SELECT i.id, i.customer_name, i.status, 
                   SUM(l.quantity * l.unit_price) as total_amount
            FROM invoice i
            JOIN invoice_line l ON i.id = l.invoice_id
            WHERE i.status IN ('CONFIRMED', 'PAID')
            GROUP BY i.id, i.customer_name, i.status
            ORDER BY i.id;
        """;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                totals.add(new InvoiceTotal(
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        rs.getString("status"),
                        rs.getBigDecimal("total_amount")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totals;
    }

    // --- Q3 : Totaux cumulés par statut (Une seule ligne) ---
    public InvoiceStatusTotals computeStatusTotals() {
        String sql = """
            SELECT 
                SUM(CASE WHEN i.status = 'PAID' THEN l.quantity * l.unit_price ELSE 0 END) as total_paid,
                SUM(CASE WHEN i.status = 'CONFIRMED' THEN l.quantity * l.unit_price ELSE 0 END) as total_confirmed,
                SUM(CASE WHEN i.status = 'DRAFT' THEN l.quantity * l.unit_price ELSE 0 END) as total_draft
            FROM invoice i
            JOIN invoice_line l ON i.id = l.invoice_id;
        """;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return new InvoiceStatusTotals(
                        rs.getBigDecimal("total_paid"),
                        rs.getBigDecimal("total_confirmed"),
                        rs.getBigDecimal("total_draft")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // --- Q4 : Chiffre d’affaires pondéré ---
    public Double computeWeightedTurnover() {
        String sql = """
            SELECT SUM(
                (l.quantity * l.unit_price) * CASE 
                    WHEN i.status = 'PAID' THEN 1.0
                    WHEN i.status = 'CONFIRMED' THEN 0.5
                    ELSE 0
                END
            ) as weighted_ca
            FROM invoice i
            JOIN invoice_line l ON i.id = l.invoice_id;
        """;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("weighted_ca");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // --- Q5-A : Totaux HT, TVA et TTC par facture ---
    public List<InvoiceTaxSummary> findInvoiceTaxSummaries() {
        List<InvoiceTaxSummary> summaries = new ArrayList<>();
        String sql = """
        SELECT 
            i.id, 
            SUM(il.quantity * il.unit_price) AS total_ht,
            SUM(il.quantity * il.unit_price) * (t.rate / 100) AS total_tva,
            SUM(il.quantity * il.unit_price) * (1 + t.rate / 100) AS total_ttc
        FROM invoice i
        JOIN invoice_line il ON i.id = il.invoice_id
        CROSS JOIN tax_config t
        WHERE t.label = 'TVA STANDARD'
        GROUP BY i.id, t.rate
        ORDER BY i.id;
    """;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                summaries.add(new InvoiceTaxSummary(
                        rs.getInt("id"),
                        rs.getBigDecimal("total_ht"),
                        rs.getBigDecimal("total_tva"),
                        rs.getBigDecimal("total_ttc")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur Q5-A : " + e.getMessage());
        }
        return summaries;
    }

    // --- Q5-B : Chiffre d’affaires TTC pondéré ---
    public BigDecimal computeWeightedTurnoverTtc() {
        String sql = """
        SELECT SUM(
            CASE 
                WHEN i.status = 'PAID' THEN (il.quantity * il.unit_price) * (1 + t.rate / 100) * 1.0
                WHEN i.status = 'CONFIRMED' THEN (il.quantity * il.unit_price) * (1 + t.rate / 100) * 0.5
                ELSE 0 
            END
        ) AS weighted_turnover_ttc
        FROM invoice i
        JOIN invoice_line il ON i.id = il.invoice_id
        CROSS JOIN tax_config t
        WHERE t.label = 'TVA STANDARD';
    """;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getBigDecimal("weighted_turnover_ttc");
            }
        } catch (SQLException e) {
            System.err.println("Erreur Q5-B : " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }
}
