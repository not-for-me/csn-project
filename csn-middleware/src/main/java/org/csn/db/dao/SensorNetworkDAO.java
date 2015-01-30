package org.csn.db.dao;

import org.csn.data.ReturnType;
import org.csn.data.SensorNetwork;
import org.csn.db.CSNDAOFactory;
import org.csn.db.ConnectionMaker;
import org.csn.util.NetworkIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class SensorNetworkDAO {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	private TagDAO tagDAO;
	private ConnectionMaker connectionMaker;

	public SensorNetworkDAO(ConnectionMaker connectionMaker) {
		this.connectionMaker = connectionMaker;
		this.tagDAO = new CSNDAOFactory().semanticConceptDAO();
	}

	public boolean isNetworkID(String id) {
		boolean ret;
		try {
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT EXISTS(SELECT id  FROM csn_sn WHERE id = ?)");
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			rs.next();
			ret = rs.getBoolean(1);

			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			ret = false;
		}
		return ret;
	}

	public String registerNetwork(String name, int cnt) {
		String id = NetworkIDGenerator.getUUID();

		try {
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("INSERT INTO csn_sn (id, name, child_net_cnt, reg_time, status) VALUES(?, ?, ?, NOW(), 'Operating')");
			ps.setString(1, id);
			ps.setString(2, name);
			ps.setInt(3, cnt);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				id = Integer.toString(rs.getInt(1));
			}
			ps.close();
			rs.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			id = null;
		} finally {
			return id;
		}
	}

	public ReturnType addMember(String id, Set<String> members) {
		ReturnType ret = ReturnType.Done;
		try {
			if (members != null) {
				for (String member : members) {
					ret = _addMember(id, member);
					if (ret == ReturnType.Error)
						throw new Exception(
								"Can't set a Network Members to the Sensor Network");
				}
			} else {
				throw new Exception("There is no member in Set!!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ret = ReturnType.Error;
		}
		return ret;
	}

	private ReturnType _addMember(String id, String member) {
		ReturnType ret = ReturnType.Done;
		try {
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("INSERT INTO csn_sn_member (parent_id, member_id) VALUES(?, ?)");
			ps.setString(1, id);
			ps.setString(2, member);

			ps.executeUpdate();
			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			ret = ReturnType.Error;
		}
		return ret;
	}

	public ReturnType updateTopicPath(String id, String topicPath) {
		ReturnType ret = ReturnType.Done;
		try {
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("UPDATE csn_sn SET topic_path = ? WHERE id = ?");
			ps.setString(1, topicPath);
			ps.setString(2, id);

			ps.executeUpdate();
			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			ret = ReturnType.Error;
		}
		return ret;
	}

	public Set<SensorNetwork> getAllNetworks() {
		Set<SensorNetwork> set = new HashSet<>();

		try {
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT * FROM csn_sn WHERE status = 'Operating'");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String id = rs.getString("id");
				Set<String> memberList = this.getMemberIDs(id);
				Set<String> tagSet = tagDAO.getAllTags(id);
				Set<Map<String, String>> metadataSet = this.getMetadata(id);
				SensorNetwork network = new SensorNetwork(id,
						rs.getString("name"), rs.getString("reg_time"),
						rs.getString("dereg_time"), rs.getString("status"),
						rs.getInt("topic_id"), rs.getString("topic_path"),
						rs.getInt("child_net_cnt"), memberList, tagSet,
						metadataSet);
				set.add(network);
			}

			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			set = null;
		}
		return set;
	}

	public Set<SensorNetwork> getNetworks(int index, int num) {
		Set<SensorNetwork> set = new HashSet<>();

		try {
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT * FROM csn_sn WHERE status = 'Operating' ORDER BY id ASC LIMIT ?, ?");
			ps.setInt(1, index);
			ps.setInt(2, num);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String id = rs.getString("id");
				Set<String> memberList = this.getMemberIDs(id);
				Set<String> tagSet = tagDAO.getAllTags(id);
				Set<Map<String, String>> metadataSet = this.getMetadata(id);
				SensorNetwork network = new SensorNetwork(id,
						rs.getString("name"), rs.getString("reg_time"),
						rs.getString("dereg_time"), rs.getString("status"),
						rs.getInt("topic_id"), rs.getString("topic_path"),
						rs.getInt("child_net_cnt"), memberList, tagSet,
						metadataSet);
				set.add(network);
			}

			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			set = null;
		}
		return set;
	}

	public ReturnType deactivateAllNetworks() {
		ReturnType ret = ReturnType.Done;
		try {
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("UPDATE csn_sn SET status = 'Stopped', dereg_time = now()");

			ps.executeUpdate();
			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			ret = ReturnType.Error;
		}
		return ret;

	}

	public ReturnType removeAllNetworks() {
		ReturnType ret = ReturnType.Done;
		try {
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c.prepareStatement("DELETE FROM csn_sn");

			ps.executeUpdate();
			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			ret = ReturnType.Error;
		}
		return ret;
	}

	public Set<String> getAllNetworkIDs() {
		Set<String> idSet = null;
		try {
			idSet = new HashSet<>();
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT id FROM csn_sn WHERE status = 'Operating'");
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				idSet.add(rs.getString("id"));

			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return idSet;
	}

	public Set<String> getNetworkIDs(int index, int num) {
		Set<String> idSet = null;
		try {
			idSet = new HashSet<>();
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT id FROM csn_sn WHERE status='Operating' ORDER BY id ASC LIMIT ?, ?");
			ps.setInt(1, index);
			ps.setInt(2, num);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				idSet.add(rs.getString("id"));

			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return idSet;
	}

	public Set<String> getAllNetworkNames() {
		Set<String> nameSet = null;
		try {
			nameSet = new HashSet<String>();
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT name FROM csn_sn WHERE status = 'Operating'");
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				nameSet.add(rs.getString("id"));

			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nameSet;
	}

	public Set<String> getNetworkNames(int index, int num) {
		Set<String> nameSet = null;
		try {
			nameSet = new HashSet<String>();
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT name FROM csn_sn WHERE status='Operating' ORDER BY id ASC LIMIT ?, ?");
			ps.setInt(1, index);
			ps.setInt(2, num);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				nameSet.add(rs.getString("id"));

			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nameSet;
	}

	public Set<String> getAllMemberNetworkIDs() {
		Set<String> idSet = null;
		try {
			idSet = new HashSet<String>();
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT DISTINCT member_id FROM csn_sn_member");
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				idSet.add(rs.getString("member_id"));

			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return idSet;
	}

	public Set<String> getAllNetworkTopicPaths() {
		Set<String> topicPathSet;
		try {
			topicPathSet = new HashSet<String>();
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT topic_path FROM csn_sn WHERE status='Operating'");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				topicPathSet.add(rs.getString("topic_path"));
			}

			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			topicPathSet = null;
		}
		return topicPathSet;
	}

	public Set<String> getNetworkTopicPaths(int index, int num) {
		Set<String> topicPathSet;
		try {
			topicPathSet = new HashSet<>();
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT topic_path FROM csn_sn WHERE status='Operating' ORDER BY id ASC LIMIT ?, ?");
			ps.setInt(1, index);
			ps.setInt(2, num);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				topicPathSet.add(rs.getString("topic_path"));
			}

			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			topicPathSet = null;
		}
		return topicPathSet;
	}

	public Map<String, Set<String>> getAllNetworkAndMemberIDs() {
		Map<String, Set<String>> map = new HashMap<String, Set<String>>();
		try {
			Set<String> idSet = this.getAllNetworkIDs();
			for (String id : idSet) {
				if (this.hasMembers(id)) {
					Set<String> members = this.getMemberIDs(id);
					map.put(id, members);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			map = null;
		}
		return map;
	}

	public Map<String, Set<String>> getAllNetworkAndMemberNames() {
		Map<String, Set<String>> map = new HashMap<String, Set<String>>();
		try {
			Set<String> idSet = this.getAllNetworkIDs();
			for (String id : idSet) {
				if (this.hasMembers(id)) {
					Set<String> memberNames = this.getMemberNames(id);
					String name = this.getNetworkName(id);
					map.put(name, memberNames);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			map = null;
		}
		return map;
	}

	public Map<String, Set<String>> getAllTopicPathAndMemberIDs() {
		Map<String, Set<String>> map = new HashMap<String, Set<String>>();
		try {
			Set<String> idSet = this.getAllNetworkIDs();
			for (String id : idSet) {
				if (this.hasMembers(id)) {
					String topicPath = this.getTopicPath(id);
					Set<String> members = this.getMemberIDs(id);
					map.put(topicPath, members);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			map = null;
		}
		return map;
	}

	public int getOperatingNetworkCount() {
		int count = 0;
		try {
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT COUNT(id) FROM csn_sn WHERE status='Operating'");
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				count = rs.getInt(1);

			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public int getStoppedNetworkCount() {
		int count = 0;
		try {
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT COUNT(id) FROM csn_sn WHERE status='Stopped'");
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				count = rs.getInt(1);

			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public int getFaultedNetworkCount() {
		int count = 0;
		try {
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT COUNT(id) FROM csn_sn WHERE status='Faulted'");
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				count = rs.getInt(1);

			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public int getAllNetworkCount() {
		int count = 0;
		try {
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT COUNT(id) FROM csn_sn");
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				count = rs.getInt(1);

			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public SensorNetwork getNetwork(String id) {
		SensorNetwork network = null;
		try {
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT * FROM csn_sn WHERE status = 'Operating' AND id = ?");
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Set<String> memberList = this.getMemberIDs(id);
				Set<String> tagSet = tagDAO.getAllTags(id);
				Set<Map<String, String>> metadataSet = this.getMetadata(id);
				network = new SensorNetwork(id, rs.getString("name"),
						rs.getString("reg_time"), rs.getString("dereg_time"),
						rs.getString("status"), rs.getInt("topic_id"),
						rs.getString("topic_path"), rs.getInt("child_net_cnt"),
						memberList, tagSet, metadataSet);
			}

			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			network = null;
		}
		return network;
	}

	public String getNetworkName(String id) {
		String name = null;
		try {
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT name FROM csn_sn WHERE status = 'Operating' AND id = ?");
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				name = rs.getString("name");

			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			name = null;
		}
		return name;
	}

	public ReturnType removeNetwork(String id) {
		ReturnType ret = ReturnType.Done;

		try {
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("UPDATE csn_sn SET status = 'Stopped' WHERE id = ?");
			ps.setString(1, id);
			ps.executeUpdate();

			ps = c.prepareStatement("DELETE FROM csn_sn_member WHERE parent_id = ?");
			ps.setString(1, id);
			ps.execute();

			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			ret = ReturnType.Error;
		}
		return ret;
	}

	public boolean isItNetwork(String id) {
		boolean ret;
		try {
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT EXISTS(SELECT *  FROM csn_sn WHERE status = 'Operating' AND id = ?)");
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			rs.next();
			ret = rs.getBoolean(1);

			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			ret = false;
		}
		return ret;
	}

	public Set<SensorNetwork> getParentNetworks(String id) {
		Set<SensorNetwork> set = new HashSet<SensorNetwork>();
		try {
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT parent_id FROM csn_sn_member WHERE member_id = ?");
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				set.add(this.getNetwork(rs.getString(1)));

			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			set = null;
		}
		return set;
	}

	public Set<String> getParentNetworkIDs(String id) {
		Set<String> set = new HashSet<String>();
		try {
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT parent_id FROM csn_sn_member WHERE member_id = ?");
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				set.add(rs.getString(1));

			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			set = null;
		}
		return set;
	}

	public Set<String> getParentNetworkNames(String id) {
		Set<String> set = new HashSet<String>();
		try {
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT parent_id FROM csn_sn_member WHERE member_id = ?");
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				set.add(this.getNetworkName(rs.getString(1)));

			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			set = null;
		}
		return set;
	}

	public Set<String> getParentNetworkTopicPaths(String id) {
		Set<String> set = new HashSet<String>();
		try {
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT parent_id FROM csn_sn_member WHERE member_id = ?");
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				set.add(this.getTopicPath(rs.getString(1)));

			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			set = null;
		}
		return set;
	}

	public boolean hasMembers(String id) {
		boolean ret = false;
		try {
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT EXISTS(SELECT member_id FROM csn_sn_member WHERE parent_id = ?)");
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			rs.next();
			ret = rs.getBoolean(1);
			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			ret = false;
		}
		return ret;
	}

	public Set<SensorNetwork> getMembers(String id) {
		Set<SensorNetwork> set = new HashSet<SensorNetwork>();
		try {
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT member_id FROM csn_sn_member WHERE parent_id = ?");
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				set.add(this.getNetwork(rs.getString(1)));

			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			set = null;
		}
		return set;
	}

	public Set<String> getMemberIDs(String id) {
		Set<String> set = new HashSet<String>();
		try {
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT member_id FROM csn_sn_member WHERE parent_id = ?");
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String memberID = rs.getString(1);
				if (!memberID.equals(id))
					set.add(rs.getString(1));
			}

			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			set = null;
		}
		return set;
	}

	public Set<String> getMemberNames(String id) {
		Set<String> set = new HashSet<String>();
		try {
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT member_id FROM csn_sn_member WHERE parent_id = ?");
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				set.add(this.getNetworkName(rs.getString(1)));

			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			set = null;
		}
		return set;
	}

	public String getTopicPath(String id) {
		String topicPath = null;
		try {
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT topic_path FROM csn_sn WHERE status = 'Operating' AND id = ?");
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			rs.next();
			topicPath = rs.getString(1);
			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			topicPath = null;
		}
		return topicPath;
	}

	public boolean hasMetaKey(String id, String key) {
		boolean ret = false;
		try {
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT COUNT(*) FROM csn_sn_meta WHERE sn_id = ? AND meta_key = ?");

			ps.setString(1, id);
			ps.setString(2, key);
			ResultSet rs = ps.executeQuery();
			rs.next();
			ret = rs.getBoolean(1);
			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			ret = false;
		}
		return ret;
	}

	public ReturnType addMetadata(String id, String key, String value) {
		ReturnType ret = ReturnType.Done;
		try {

			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = null;
			if (this.hasMetaKey(id, key)) {
				ps = c.prepareStatement("UPDATE csn_sn_meta SET meta_value =? WHERE sn_id = ? AND meta_key = ?)");
				ps.setString(1, value);
				ps.setString(2, id);
				ps.setString(3, key);
			} else {
				ps = c.prepareStatement("INSERT INTO csn_sn_meta (sn_id, meta_key, meta_value) VALUES(?, ?, ?)");
				ps.setString(1, id);
				ps.setString(2, key);
				ps.setString(3, value);
			}

			ps.executeUpdate();
			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			ret = ReturnType.Error;
		}
		return ret;
	}

	public ReturnType addMetadata(String id,
			Set<Map<String, String>> metadataSet) {
		ReturnType ret = ReturnType.Done;
		try {
			for (Map<String, String> metadata : metadataSet) {
				ret = this.addMetadata(id, metadata.get("metaKey"),
						metadata.get("metaValue"));
				if (ret == ReturnType.Error)
					throw new Exception("Can't Add a Metadata");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ret = null;
		}

		return ret;
	}

	public Set<Map<String, String>> getMetadata(String id) {
		Set<Map<String, String>> metadataSet = new HashSet<Map<String, String>>();
		try {
			Connection c = connectionMaker.makeConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT meta_key, meta_value FROM csn_sn_meta WHERE sn_id = ?");
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Map<String, String> newItem = new HashMap<String, String>();
				newItem.put("metaKey", rs.getString(1));
				newItem.put("metaValue", rs.getString(2));
				metadataSet.add(newItem);
			}

			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			metadataSet = null;
		}
		return metadataSet;
	}

}
