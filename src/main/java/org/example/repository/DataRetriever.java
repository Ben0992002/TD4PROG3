package org.example.repository;

import org.example.model.CandidateVoteCount;
import org.example.model.VoteTypeCount;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    private final Connection connection;

    public DataRetriever(Connection connection) {
        this.connection = connection;
    }

    public List<VoteTypeCount> fetchVoteStats() throws SQLException {
        // CORRECTION : vote_type -> type_vote
        String sql = "SELECT type_vote, COUNT(*) as total FROM vote GROUP BY type_vote";
        List<VoteTypeCount> list = new ArrayList<>();

        try (Statement s = connection.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new VoteTypeCount(
                        rs.getString("type_vote"), // CORRECTION ICI
                        rs.getLong("total")
                ));
            }
        }
        return list;
    }

    public List<CandidateVoteCount> fetchResults() throws SQLException {
        // CORRECTION : vote_type -> type_vote (3 fois dans cette requête)
        String sql = """
            SELECT 
                c.name, 
                COUNT(v.id) as score,
                (COUNT(v.id) * 100.0 / NULLIF((SELECT COUNT(*) FROM vote WHERE type_vote = 'VALID'), 0)) as pct
            FROM candidate c
            LEFT JOIN vote v ON c.id = v.candidate_id AND v.type_vote = 'VALID'
            GROUP BY c.name 
            ORDER BY score DESC
            """;
        List<CandidateVoteCount> list = new ArrayList<>();

        try (Statement s = connection.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new CandidateVoteCount(
                        rs.getString("name"),
                        rs.getLong("score"),
                        rs.getDouble("pct")
                ));
            }
        }
        return list;
    }
}
