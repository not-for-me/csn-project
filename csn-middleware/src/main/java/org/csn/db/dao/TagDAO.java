package org.csn.db.dao;

import org.csn.data.ReturnType;
import org.csn.db.ConnectionMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;


public class TagDAO {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private ConnectionMaker connectionMaker;

    public TagDAO(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public ReturnType addTag(String tag, String id) {
        ReturnType ret = ReturnType.Done;
        try {
            String tag_id = this.getTagID(tag);
            if(tag_id == null) {
                this.createTag(tag);
                tag_id = this.getTagID(tag);
            }

            if(this.hasTag(tag, id))
                return ReturnType.Error;

            Connection c = connectionMaker.makeConnection();
            PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO csn_annotate (tag_id, sn_id) VALUES(?, ?)");
            ps.setString(1, tag_id);
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

    public ReturnType createTag(String tag) {
        ReturnType ret = ReturnType.Done;
        try {
            Connection c = connectionMaker.makeConnection();
            PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO csn_tag (name) VALUES (?)");
            ps.setString(1, tag);
            ps.executeUpdate();
            ps.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            ret = ReturnType.Error;
        }
        return ret;
    }

    public String getTagID(String tag) {
        String id = null;
        try {
            Connection c = connectionMaker.makeConnection();
            PreparedStatement ps = c.prepareStatement(
                    "SELECT id FROM csn_tag WHERE name = ?");
            ps.setString(1, tag);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
                id = rs.getString(1);
            ps.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            id = null;
        }
        return id;
    }

    protected String _getConceptName(String id) {
        String tag;
        try {
            Connection c = connectionMaker.makeConnection();
            PreparedStatement ps = c.prepareStatement(
                    "SELECT name FROM csn_tag WHERE id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            tag = rs.getString(1);
            ps.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            tag = null;
        }
        return tag;
    }

    public ReturnType addTag(Set<String> tagSet, String id) {
        ReturnType ret = ReturnType.Done;
        try {
            for(String tag : tagSet) {
                ret = this.addTag(tag, id);
                if(ret == ReturnType.Error)
                    throw new Exception("Can't Add a tag");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = null;
        }

        return ret;
    }

    public ReturnType addTag(String tag, Set<String> idSet) {
        ReturnType ret = ReturnType.Done;
        try {
            for(String id : idSet) {
                ret = this.addTag(tag, id);
                if(ret == ReturnType.Error)
                    throw new Exception("Can't Add a tag");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = null;
        }

        return ret;
    }

    public boolean hasTag(String tag, String id) {
        boolean ret = false;
        try {
            Connection c = connectionMaker.makeConnection();
            PreparedStatement ps = c.prepareStatement(
                    "SELECT COUNT(*) FROM csn_annotate WHERE tag_id = ? AND sn_id = ?");
            String tag_id = this.getTagID(tag);
            if(tag_id == null)
                return false;

            ps.setString(1, tag_id);
            ps.setString(2, id);
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

    public Set<String> getAllTags() {
        Set<String> tagSet = new HashSet<String>();
        try {
            Connection c = connectionMaker.makeConnection();
            PreparedStatement ps = c.prepareStatement(
                    "SELECT name FROM csn_tag");
            ResultSet rs = ps.executeQuery();
            while(rs.next())
                tagSet.add(rs.getString(1));

            ps.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            tagSet = null;
        }
        return tagSet;
    }

    public Set<String> getAllTags(String id) {
        Set<String> tagSet = new HashSet<String>();
        try {
            Connection c = connectionMaker.makeConnection();
            PreparedStatement ps = c.prepareStatement(
                    "SELECT tag_id FROM csn_annotate WHERE sn_id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
                tagSet.add(this._getConceptName(rs.getString(1)));

            ps.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            tagSet = null;
        }
        return tagSet;
    }

    public ReturnType removeTag(String tag) {
        ReturnType ret = ReturnType.Done;
        try {
            Connection c = connectionMaker.makeConnection();
            PreparedStatement ps = c.prepareStatement(
                    "DELETE FROM csn_tag WHERE name = ?");
            ps.setString(1, tag);

            ps.executeUpdate();
            ps.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            ret = ReturnType.Error;
        }
        return ret;
    }

    public ReturnType removeTag(String tag, String id) {
        ReturnType ret = ReturnType.Done;
        try {
            Connection c = connectionMaker.makeConnection();
            PreparedStatement ps = c.prepareStatement(
                    "DELETE FROM csn_annotate WHERE tag_id = ? AND  sn_id = ?");
            String tag_id = this.getTagID(tag);
            ps.setString(1, tag_id);
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

    public ReturnType removeAllTags(String id) {
        ReturnType ret = ReturnType.Done;
        try {
            Connection c = connectionMaker.makeConnection();
            PreparedStatement ps = c.prepareStatement(
                    "DELETE FROM csn_annotate WHERE sn_id = ?");
            ps.setString(1, id);

            ps.executeUpdate();
            ps.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            ret = ReturnType.Error;
        }
        return ret;
    }


}
