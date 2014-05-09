package org.jobimtext.api.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jobimtext.api.IThesaurus;


public class DatabaseThesaurus extends DatabaseResource
		implements
		IThesaurus<String, String, ResultSet, ResultSet, ResultSet, ResultSet, ResultSet> {

	@Override
	public ResultSet getSimilarTerms(String key) {
		String sql = getDatabaseConfiguration().getSimilarTermsQuery();
		try {
			
			
			PreparedStatement ps;
			ps = getDatabaseConnection().getConnection().prepareStatement(sql);
			ps.setString(1, key);
			return ps.executeQuery();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.err.println("Query: "+sql);
		}
		return null;
	}

	@Override
	public ResultSet getSimilarTerms(String key, int numberOfEntries) {
		String sql = getDatabaseConfiguration().getSimilarTermsTopQuery(
				numberOfEntries);
		try {
			
			PreparedStatement ps = getDatabaseConnection().getConnection()
					.prepareStatement(sql);
			ps.setString(1, key);
			return ps.executeQuery();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.err.println("Query: "+sql);
		}
		return null;
	}

	@Override
	public ResultSet getSimilarTerms(String key, double threshold) {
		String sql = getDatabaseConfiguration().getSimilarTermsGtScoreQuery();
		
		try {
			PreparedStatement ps = getDatabaseConnection().getConnection()
					.prepareStatement(sql);
			ps.setString(1, key);
			ps.setDouble(2, threshold);
			return ps.executeQuery();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.err.println("Query: "+sql);
		}
		return null;
	}

	@Override
	public ResultSet getSimilarContexts(String values) {
		String sql = getDatabaseConfiguration().getSimilarContextsQuery();
		
		try {
			PreparedStatement ps = getDatabaseConnection().getConnection().prepareStatement(sql);
			ps.setString(1, values);
			return ps.executeQuery();
		} catch (SQLException e) {
			
			System.err.println(e.getMessage());
			System.err.println(values);
			System.err.println("Query: "+sql);
		}
		return null;
	}

	@Override
	public ResultSet getSimilarContexts(String values, int numberOfEntries) {
		String sql = getDatabaseConfiguration().getSimilarContextsTopQuery(numberOfEntries);
		
		try {
			
			PreparedStatement ps = getDatabaseConnection().getConnection().prepareStatement(sql);
			ps.setString(1, values);
			
			return ps.executeQuery();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.err.println(values);
			System.err.println("Query: "+sql);
		}
		return null;
	}

	@Override
	public ResultSet getSimilarContexts(String values, double threshold) {
		String sql = getDatabaseConfiguration().getSimilarContextsGtScoreQuery();
		
		try {
			
			PreparedStatement ps = getDatabaseConnection().getConnection().prepareStatement(sql);
			ps.setString(1, values);
			ps.setDouble(2, threshold);
			
			return ps.executeQuery();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.err.println("Query: "+sql);
		}
		return null;
	}

	@Override
	public Long getTermCount(String key) {
		Long count = 0L;
		String sql = getDatabaseConfiguration().getTermsCountQuery();
		PreparedStatement ps;
		try {
			ps = getDatabaseConnection().getConnection().prepareStatement(sql);

			ps.setString(1, key);
			ResultSet set = ps.executeQuery();

			if (set.next()) {
				count = set.getLong(1);
			}
			ps.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.err.println("Query: "+sql);
		}
		return count;
	}

	@Override
	public Long getContextsCount(String values) {
		Long count = 0L;
		String sql = getDatabaseConfiguration().getContextsCountQuery();
		PreparedStatement ps;
		try {
			ps = getDatabaseConnection().getConnection().prepareStatement(sql);
			ps.setString(1, values);
			ResultSet set = ps.executeQuery();
			if (set.next()) {
				count = set.getLong(1);
			}
			ps.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.err.println("Query: "+sql);
		}
		return count;
	}

	@Override
	public Long getTermContextsCount(String key, String values) {
		Long count = 0L;
		String sql = getDatabaseConfiguration().getTermContextsCountQuery();
		PreparedStatement ps;
		try {
			ps = getDatabaseConnection().getConnection().prepareStatement(sql);
			ps.setString(1, key);
			ps.setString(2, values);
			ResultSet set = ps.executeQuery();
			if (set.next()) {
				count = set.getLong(1);
			}
			ps.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.err.println("Query: "+sql);
		}
		return count;
	}

	@Override
	public Double getTermContextsScore(String key, String val) {
		double score = 0.0;
		String sql = getDatabaseConfiguration().getTermContextsScoreQuery();
		PreparedStatement ps;
		try {
			ps = getDatabaseConnection().getConnection().prepareStatement(sql);
			ps.setString(1, key);
			ps.setString(2, val);
			ResultSet set = ps.executeQuery();
			if (set.next()) {
				score = set.getDouble(1);

			}
			ps.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.err.println("Query: "+sql);
		}
		return score;
	}

	@Override
	public ResultSet getTermContextsScores(String key) {
		String sql = getDatabaseConfiguration().getTermContextsScoresQuery();
		PreparedStatement ps;
		try {
			ps = getDatabaseConnection().getConnection().prepareStatement(sql);
			ps.setString(1, key);
			ResultSet set = ps.executeQuery();
			
			return set;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.err.println("Query: "+sql);
		}
		return null;
	}

	@Override
	public ResultSet getTermContextsScores(String key, int numberOfEntries) {
		String sql;
		sql = getDatabaseConfiguration().getTermContextsScoresTopQuery(numberOfEntries);
		PreparedStatement ps;
		try {
			ps = getDatabaseConnection().getConnection().prepareStatement(sql);
			ps.setString(1, key);
			ResultSet set = ps.executeQuery();
			
			return set;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.err.println("  Query: "+sql);
		}
		return null;
	}

	@Override
	public ResultSet getTermContextsScores(String key, double threshold) {
		String sql = getDatabaseConfiguration().getTermContextsScoresGtScore();
		PreparedStatement ps;
		try {
			ps = getDatabaseConnection().getConnection().prepareStatement(sql);
			ps.setString(1, key);
			ps.setDouble(2, threshold);
			ResultSet set = ps.executeQuery();
			
			return set;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.err.println("Query: "+sql);
		}
		return null;
	}

	@Override
	public ResultSet getSenses(String key) {
		String sql = getDatabaseConfiguration().getSensesQuery();
		PreparedStatement ps;
		try {
			ps = getDatabaseConnection().getConnection().prepareStatement(sql);
			ps.setString(1, key);
			ResultSet set = ps.executeQuery();
			
			return set;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.err.println("Query: "+sql);
		}
		return null;
	}

	@Override
	public ResultSet getIsas(String key) {
		String sql = getDatabaseConfiguration().getIsasQuery();
		PreparedStatement ps;
		try {
			ps = getDatabaseConnection().getConnection().prepareStatement(sql);
			ps.setString(1, key);
			ResultSet set = ps.executeQuery();
			
			return set;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.err.println("Query: "+sql);
		}
		return null;
	}

	@Override
	public ResultSet getSenseCUIs(String key) {
		String sql = getDatabaseConfiguration().getSensesCUIsQuery();
		PreparedStatement ps;
		try {
			ps = getDatabaseConnection().getConnection().prepareStatement(sql);
			ps.setString(1, key);
			ResultSet set = ps.executeQuery();
			
			return set;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.err.println("Query: "+sql);
		}
		return null;
	}

	@Override
	public Double getSimilarTermScore(String t1, String t2) {
		double score = 0.0;
		String sql = getDatabaseConfiguration().getSimilarTermScoreQuery();
		PreparedStatement ps;
		try {
			ps = getDatabaseConnection().getConnection().prepareStatement(sql);
			ps.setString(1, t1);
			ps.setString(2, t2);
			ResultSet set = ps.executeQuery();
			if (set.next()) {
				score = set.getDouble(1);

			}
			ps.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.err.println("Query: "+sql);
		}
		return score;
	}

}
