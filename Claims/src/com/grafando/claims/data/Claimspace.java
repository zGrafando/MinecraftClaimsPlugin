package com.grafando.claims.data;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Properties;

import static java.time.LocalDateTime.*;

public class Claimspace {
    PreparedStatement ps = null;
    static PreparedStatement sps = null;

    String username;
    String password;
    String serverName;
    String dbms;

    String SQLSelectClaimblocksQuery = "SELECT Claimblocks FROM players Where UUID = ?;";
    String SQLSelectClaimogr = "SELECT Block_World, Block_X, Block_Y, Block_Z from claimography where ClaimownerUUID = ?;";
    String SQLEvaluateClaim = "SELECT * from claims WHERE (Block1_World = ? AND Block2_World = ?) AND " +
            "((? >= Block1_X AND ? <= Block2_X) AND (? >= Block1_Z AND ? <= Block2_Z)) OR " +
            "((? >= Block1_X AND ? <= Block2_X) AND (? <= Block1_Z AND ? >= Block2_Z)) OR" +
            "((? <= Block1_X AND ? >= Block2_X) AND (? >= Block1_Z AND ? <= Block2_Z)) OR" +
            "((? <= Block1_X AND ? >= Block2_X) AND (? <= Block1_Z AND ? >= Block2_Z))";
    String SQLDeleteSpecificClaim = "DELETE from claims WHERE ClaimId = ?";
    String SQLSelectSpecificClaim =  "SELECT Block1_X, Block2_X, Block1_Z, Block2_Z from claims WHERE ClaimId = (SELECT ClaimId from claims " +
            "where (Block1_World = ? AND Block2_World = ?) AND" +
            "((? >= Block1_X AND ? <= Block2_X) AND (? >= Block1_Z AND ? <= Block2_Z)) OR" +
            "((? >= Block1_X AND ? <= Block2_X) AND (? <= Block1_Z AND ? >= Block2_Z)) OR" +
            "((? <= Block1_X AND ? >= Block2_X) AND (? >= Block1_Z AND ? <= Block2_Z)) OR" +
            "((? <= Block1_X AND ? >= Block2_X) AND (? <= Block1_Z AND ? >= Block2_Z)) AND ClaimownerUUID = ?);";
    String SQLInsertClaimography = "INSERT into claimography (Block_World, Block_X, Block_Y, Block_Z, ClaimownerUUID) VALUES (?, ?, ?, ?, ?);";
    String SQLInsertClaim = "INSERT into claims (Block1_World, Block1_X, Block1_Y, Block1_Z, Block2_World, Block2_X, Block2_Y, Block2_Z, " +
            "Claimname, Claimowner, ClaimownerUUID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    String SQLSelectBalanceQuery = "SELECT Balance FROM players Where UUID = ?;";
    static String SQLSelectBalanceQuery2 = "SELECT Balance FROM players Where UUID = ?;";
    String SQLSelectMoneyAmountRemaining = "SELECT MoneyAmount FROM world;";
    String SQLUpdateBalance = "UPDATE players SET Balance = ? Where UUID = ?;";
    String SQLUpdateClaimblocks = "Update players SET Claimblocks = ? Where PlayerId = ?;";
    String SQLSelectClaimblockRatio = "SELECT ClaimblockRatio FROM world;";
    String SQLSelectWorldFromClaimogr = "SELECT Block_World from claimography where ClaimownerUUID = ?;";
    String SQLSelectXFromClaimogr = "SELECT Block_X from claimography where ClaimownerUUID = ?";
    String SQLSelectYFromClaimogr = "SELECT Block_Y from claimography where ClaimownerUUID = ?";
    String SQLSelectZFromClaimogr = "SELECT Block_Z from claimography where ClaimownerUUID = ?";
    String SQLDeleteClaimography = "DELETE from claimography where ClaimogrId = ?;";
    String SQLGetClaimogrId = "SELECT ClaimogrId from claimography where ClaimownerUUID = ?";
    String SQlSelectFirstClaim = "SELECT ClaimId FROM claims WHERE ClaimownerUUID = ?;";
    String SQLDeleteClaimById = "Delete from claims where ClaimId = ?;";
    String SQLSelectBlock1X = "SELECT Block1_X from claims where ClaimId = ?;";
    String SQLSelectBlock2X = "SELECT Block2_X from claims where ClaimId = ?;";
    String SQLSelectBlock1Z = "SELECT Block1_Z from claims where ClaimId = ?;";
    String SQLSelectBlock2Z = "SELECT Block2_Z from claims where ClaimId = ?;";
    String SQLUpdateLastPlayerMovmentTimestamp = "UPDATE players set ActivityTimestamp = ? WHERE PlayerId = ?;";
    String SQLSelectPlayerTimestamp = "SELECT ActivityTimestamp from players WHERE UUID = ?;";
    String SQLSelectPlayerActivityIndicator = "SELECT ActivityIndicator from players WHERE UUID = ?;";
    String SQLIncreaseActivityIndicator = "UPDATE players SET ActivityIndicator = ? WHERE PlayerId = ?;";
    String SQLGetPlayerId = "Select PlayerId from players where UUID = ?;";
    String SQLGetClaimId = "SELECT ClaimId from claims WHERE (Block1_World = ? And Block2_World = ?) AND (? >= Block1_X AND ? <= Block2_X AND ? >= Block1_Z AND ? <= Block2_Z) OR" +
            "(? >= Block1_X AND ? <= Block2_X AND ? <= Block1_Z AND ? >= Block2_Z) OR (? <= Block1_X AND ? >= Block2_X AND ? >= Block1_Z AND ? <= Block2_Z) OR" +
            "(? <= Block1_X AND ? >= Block2_X AND ? <= Block1_Z AND ? >= Block2_Z) AND ClaimownerUUID = ?;";
    String SQLinsertTrust = "Insert into crosstrust (ForeignClaim, ForeignPlayer) VALUES (?, ?);";
    String SQLinsertGrantAccess = "Insert into crossgrantaccess (ForeignClaim, ForeignPlayer) VALUES (?, ?);";
    String SQLdeleteTrust = "DELETE from crosstrust where ForeignClaim = ? AND ForeignPlayer = ?;";
    String SQLdeleteGrantAccess = "DELETE from crossgrantaccess where ForeignClaim = ? AND ForeignPlayer = ?;";
    String SQLselectTrust = "SELECT * from crosstrust where ForeignClaim = ? AND ForeignPlayer = ?;";
    String SQLselectGrantAccess = "SELECT * from crossgrantaccess where ForeignClaim = ? AND ForeignPlayer = ?;";
    String SQLgetAllTrustsForClaimId = "SELECT ForeignPlayer, CrossTrustId from crosstrust where ForeignClaim = ? " +
            "AND CrossTrustId > ? ORDER BY CrossTrustId ASC;";
    String SQLgetAllGrantAccessForClaimId = "SELECT ForeignPlayer, CrossGrantAccessId FROM crossgrantaccess WHERE " +
            "ForeignClaim = ? AND CrossGrantAccessId > ? ORDER BY CrossGrantAccessId ASC;";
    String SQLSelectPlayerFromId = "SELECT Playername from players where PlayerId = ?;";
    String SQLSelectPlayerFromUUID = "SELECT PLayername from players where UUID = ?;";


    ResultSet rs;
    ResultSet ts;
    static ResultSet srs;
    int rowsAffected;


    public Claimspace() {
        this.password = "Nbiwe!12";
        this.username = "grafando";
        this.serverName = "jdbc:mysql://localhost:3306/Edgewind";
        this.dbms = "mysql";
    }


    public Connection getConnection(){
        Connection conn = null;
        try {
            Properties connectionParms = new Properties();
            connectionParms.put("user", this.username);
            connectionParms.put("password", this.password);
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (this.dbms.equals("mysql")) {
                conn = DriverManager.getConnection(serverName, connectionParms);
            }
            assert conn != null;
            conn.setAutoCommit(true);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return conn;
    }

    public double getClaimblockRatio(Connection connect){
        double balance = 0.0;
        try{
            ps = connect.prepareStatement(SQLSelectClaimblockRatio);
            rs = ps.executeQuery();
            if (rs.next()) {
                balance = rs.getDouble("ClaimblockRatio");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return balance;
    }

    public static boolean checkPlayerBalanceExistance(String player, Connection connec){
        boolean tri = false;
        try{
            Connection conn = connec;
            sps = conn.prepareStatement(SQLSelectBalanceQuery2);
            sps.setString(1, player);
            srs = sps.executeQuery();
            return srs.next();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return tri;
    }


    public String getPlayerById(int PlayerId,Connection connect){
        try{
            ps = connect.prepareStatement(SQLSelectPlayerFromId);
            ps.setInt(1,PlayerId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("Playername");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return "";
    }

    public String getPlayerByUUID(String playerUUID,Connection connect){
        try{
            ps = connect.prepareStatement(SQLSelectPlayerFromUUID);
            ps.setString(1, playerUUID);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("Playername");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return "";
    }


    public ArrayList<Integer> getAllTrustedParties(ArrayList<Integer> playerIdList, int claimId, int startPos, Connection connect){
        try{
            ps = connect.prepareStatement(SQLgetAllTrustsForClaimId);
            ps.setInt(1, claimId);
            ps.setInt(2, startPos);
            rs = ps.executeQuery();
            if (rs.next()) {
                playerIdList.add(rs.getInt("ForeignPlayer"));
                getAllTrustedParties(playerIdList, claimId, rs.getInt("CrossTrustId"), connect);
            }else{
                return playerIdList;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return playerIdList;
    }

    public ArrayList<Integer> getAllGrantedAccessParties(ArrayList<Integer> playerIdList, int claimId, int startPos, Connection connect){
        try{
            ps = connect.prepareStatement(SQLgetAllGrantAccessForClaimId);
            ps.setInt(1, claimId);
            ps.setInt(2, startPos);
            rs = ps.executeQuery();
            if (rs.next()) {
                playerIdList.add(rs.getInt("ForeignPlayer"));
                getAllGrantedAccessParties(playerIdList, claimId, rs.getInt("CrossGrantAccessId"), connect);
            }else{
                return playerIdList;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return playerIdList;
    }

    public int getPlayerId(String player, Connection connect){
        try{
            ps = connect.prepareStatement(SQLGetPlayerId);
            ps.setString(1, player);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("PlayerId");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    public int getClaimId(String World, double blockX, double blockZ, Connection connect, String player){
        try{
            ps = connect.prepareStatement(SQLGetClaimId);
            ps.setString(1, World);
            ps.setString(2, World);
            ps.setDouble(3, blockX);
            ps.setDouble(4, blockX);
            ps.setDouble(5, blockZ);
            ps.setDouble(6, blockZ);
            ps.setDouble(7, blockX);
            ps.setDouble(8, blockX);
            ps.setDouble(9, blockZ);
            ps.setDouble(10, blockZ);
            ps.setDouble(11, blockX);
            ps.setDouble(12, blockX);
            ps.setDouble(13, blockZ);
            ps.setDouble(14, blockZ);
            ps.setDouble(15, blockX);
            ps.setDouble(16, blockX);
            ps.setDouble(17, blockZ);
            ps.setDouble(18, blockZ);
            ps.setString(19, player);
            rs = ps.executeQuery();
            if (rs.next()){
                return rs.getInt("ClaimId");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    public boolean insertTrust(int claimId, int playerId, Connection connect){
        boolean status = false;
        try{
            ps = connect.prepareStatement(SQLinsertTrust);
            ps.setInt(1, claimId);
            ps.setInt(2, playerId);
            rowsAffected = ps.executeUpdate();
            status =  rowsAffected > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return status;
    }

    public boolean insertGrantAccess(int claimId, int playerId, Connection connect){
        boolean status = false;
        try{
            ps = connect.prepareStatement(SQLinsertGrantAccess);
            ps.setInt(1, claimId);
            ps.setInt(2, playerId);
            rowsAffected = ps.executeUpdate();
            status =  rowsAffected > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return status;
    }

    public boolean deleteTrust(int claimId, int playerId, Connection connect){
        boolean status = false;
        try{
            ps = connect.prepareStatement(SQLdeleteTrust);
            ps.setInt(1, claimId);
            ps.setInt(2, playerId);
            rowsAffected = ps.executeUpdate();
            status = rowsAffected > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return status;
    }

    public boolean deleteGrantAccess(int claimId, int playerId, Connection connect){
        boolean status = false;
        try{
            ps = connect.prepareStatement(SQLdeleteGrantAccess);
            ps.setInt(1, claimId);
            ps.setInt(2, playerId);
            rowsAffected = ps.executeUpdate();
            status =  rowsAffected > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return status;
    }

    public boolean doesTrustCrossExist(int claimId, int playerId, Connection connect){
        boolean status = false;
        try{
            ps = connect.prepareStatement(SQLselectTrust);
            ps.setInt(1, claimId);
            ps.setInt(2, playerId);
            rs = ps.executeQuery();
            status =  rs.next();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return status;
    }

    public boolean doesGrantAccessCrossExist(int claimId, int playerId, Connection connect){
        boolean status = false;
        try{
            ps = connect.prepareStatement(SQLselectGrantAccess);
            ps.setInt(1, claimId);
            ps.setInt(2, playerId);
            rs = ps.executeQuery();
            status = rs.next();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return status;
    }

    public int doesClaimExist(Player player, Connection connec){
        int id = 0;
        try{
            ps = connec.prepareStatement(SQlSelectFirstClaim);
            ps.setString(1,player.getUniqueId().toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt("ClaimId");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return id;
    }

    public boolean isClaimographySet(Player player, Connection connect){
        boolean tri = false;
        try{
            ps = connect.prepareStatement(SQLSelectClaimogr);
            ps.setString(1, player.getUniqueId().toString());
            rs = ps.executeQuery();
            if (rs.next()){
                tri = true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return tri;
    }

    public int getClaimblocks(Player player, Connection connect){
        int balance = 0;
        try{
            ps = connect.prepareStatement(SQLSelectClaimblocksQuery);
            ps.setString(1, player.getUniqueId().toString());
            rs = ps.executeQuery();
            if (rs.next()){
                balance = rs.getInt("Claimblocks");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return balance;
    }

    public void registerClaimography(String blockWorld, double blockX, double blockY, double blockZ,
                                      String claimowner, Connection connect){
        try{
            ps = connect.prepareStatement(SQLInsertClaimography);
            ps.setString(1, blockWorld);
            ps.setDouble(2, blockX);
            ps.setDouble(3, blockY);
            ps.setDouble(4, blockZ);
            ps.setString(5, claimowner);
            rowsAffected = ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public String getWorldFromClaimography(String claimowner, Connection conn){
        try{
            ps = conn.prepareStatement(SQLSelectWorldFromClaimogr);
            ps.setString(1, claimowner);
            rs = ps.executeQuery();
            if (rs.next()){
                return rs.getString(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return "world";
    }

    public double getXFromClaimography(String claimowner, Connection conn){
        double res = 0.0;
        try{
            ps = conn.prepareStatement(SQLSelectXFromClaimogr);
            ps.setString(1, claimowner);
            rs = ps.executeQuery();
            if (rs.next()){
                res = rs.getDouble(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return res;
    }

    public double getYFromClaimography(String claimowner, Connection conn){
        double res = 0.0;
        try{
            ps = conn.prepareStatement(SQLSelectYFromClaimogr);
            ps.setString(1, claimowner);
            rs = ps.executeQuery();
            if (rs.next()){
                res = rs.getDouble(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return res;
    }

    public double getZFromClaimography(String claimowner, Connection conn){
        double res = 0.0;
        try{
            ps = conn.prepareStatement(SQLSelectZFromClaimogr);
            ps.setString(1, claimowner);
            rs = ps.executeQuery();
            if (rs.next()){
                res = rs.getDouble(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return res;
    }

    public int getClaimogrIdFromClaimography(String claimowner, Connection conn){
        int res = 0;
        try{
            ps = conn.prepareStatement(SQLGetClaimogrId);
            ps.setString(1, claimowner);
            rs = ps.executeQuery();
            if (rs.next()){
                res = rs.getInt(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return res;
    }

    public String isInClaim(String World, double blockX, double blockZ, Connection connect, String modus){
        String claimowner = "";
        try{
            ps = connect.prepareStatement(SQLEvaluateClaim);
            ps.setString(1, World);
            ps.setString(2, World);
            ps.setDouble(3, blockX);
            ps.setDouble(4, blockX);
            ps.setDouble(5, blockZ);
            ps.setDouble(6, blockZ);
            ps.setDouble(7, blockX);
            ps.setDouble(8, blockX);
            ps.setDouble(9, blockZ);
            ps.setDouble(10, blockZ);
            ps.setDouble(11, blockX);
            ps.setDouble(12, blockX);
            ps.setDouble(13, blockZ);
            ps.setDouble(14, blockZ);
            ps.setDouble(15, blockX);
            ps.setDouble(16, blockX);
            ps.setDouble(17, blockZ);
            ps.setDouble(18, blockZ);
            rs = ps.executeQuery();
            if (rs.next()){
                if (modus.equalsIgnoreCase("name")) {
                    claimowner = rs.getString("Claimowner");
                }else if(modus.equalsIgnoreCase("uuid")){
                    claimowner = rs.getString("ClaimownerUUID");
                }else if(modus.equalsIgnoreCase("rentedUUID")){
                    claimowner = rs.getString("RenterUUID");
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return claimowner;
    }

    public boolean insertClaim(String world1, double block1_X, double block1_Y, double block1_Z,
                              String world2, double block2_X, double block2_Y, double block2_Z,
                              String claimname, String claimowner, Player owner, Connection conn){
        boolean tri = false;
        try{
            ps = conn.prepareStatement(SQLInsertClaim);
            ps.setString(1, world1);
            ps.setDouble(2, block1_X);
            ps.setDouble(3, block1_Y);
            ps.setDouble(4, block1_Z);
            ps.setString(5, world2);
            ps.setDouble(6, block2_X);
            ps.setDouble(7, block2_Y);
            ps.setDouble(8, block2_Z);
            ps.setString(9, claimname);
            ps.setString(10, claimowner);
            ps.setString(11, owner.getUniqueId().toString());
            rowsAffected = ps.executeUpdate();
            tri = rowsAffected > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return tri;
    }

    public boolean updateClaimblocks(Player player, int amntClaimblocks, Connection connect){
        boolean tri = false;
        try{
            int currentClaimblocks = getClaimblocks(player, connect);
            int playerId = this.getPlayerId(player.getUniqueId().toString(), connect);
            currentClaimblocks = currentClaimblocks - amntClaimblocks;
            if (currentClaimblocks >= 0) {
                PreparedStatement ps = connect.prepareStatement(SQLUpdateClaimblocks);
                ps.setInt(1, currentClaimblocks);
                ps.setInt(2, playerId);
                rowsAffected = ps.executeUpdate();
                tri = rowsAffected > 0;
            }else{
                player.sendMessage(ChatColor.translateAlternateColorCodes
                        ('&', "&4>&cClaimblocks missing: &b&l"+ currentClaimblocks));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return tri;
    }

    public int calculateClaimblocksReturned(String World, double blockX, double blockZ, Player player, Connection connect){
        int tri = 0;
        try{
            ps = connect.prepareStatement(SQLSelectSpecificClaim);
            ps.setString(1, World);
            ps.setString(2, World);
            ps.setDouble(3, blockX);
            ps.setDouble(4, blockX);
            ps.setDouble(5, blockZ);
            ps.setDouble(6, blockZ);
            ps.setDouble(7, blockX);
            ps.setDouble(8, blockX);
            ps.setDouble(9, blockZ);
            ps.setDouble(10, blockZ);
            ps.setDouble(11, blockX);
            ps.setDouble(12, blockX);
            ps.setDouble(13, blockZ);
            ps.setDouble(14, blockZ);
            ps.setDouble(15, blockX);
            ps.setDouble(16, blockX);
            ps.setDouble(17, blockZ);
            ps.setDouble(18, blockZ);
            ps.setString(19, player.getUniqueId().toString());
            rs = ps.executeQuery();
            if (rs.next()){
                if (rs.getDouble("Block1_X") > rs.getDouble("Block2_X")) {
                    int diffInX = (int)rs.getDouble("Block1_X") - (int)rs.getDouble("Block2_X");
                    int diffInZ;
                    if (rs.getDouble("Block1_Z") > rs.getDouble("Block2_Z")) {
                        diffInZ = (int)rs.getDouble("Block1_Z") - (int)rs.getDouble("Block2_Z");
                    }else{
                        diffInZ = (int)rs.getDouble("Block2_Z") - (int)rs.getDouble("Block1_Z");
                    }
                    tri = (diffInX * diffInZ) / 100 * 88;
                }else{
                    int diffInX = (int)rs.getDouble("Block2_X") - (int)rs.getDouble("Block1_X");
                    int diffInZ;
                    if (rs.getDouble("Block1_Z") > rs.getDouble("Block2_Z")) {
                        diffInZ = (int)rs.getDouble("Block1_Z") - (int)rs.getDouble("Block2_Z");
                    }else{
                        diffInZ = (int)rs.getDouble("Block2_Z") - (int)rs.getDouble("Block1_Z");
                    }
                    tri = (diffInX * diffInZ) / 100 * 88;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tri;
    }

    public boolean resetSpecificClaim(Player player, String World, double blockX, double blockZ, Connection connect){
        boolean tri = false;
        try{
            int claimId = getClaimId(World, blockX, blockZ, connect, player.getUniqueId().toString());
            ps = connect.prepareStatement(SQLDeleteSpecificClaim);
            ps.setInt(1, claimId);
            rowsAffected = ps.executeUpdate();
            tri = rowsAffected > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return tri;
    }

    public double getBalance(Player player, Connection connect){
        double balance = 0.0;
        try{
            ps = connect.prepareStatement(SQLSelectBalanceQuery);
            ps.setString(1, player.getUniqueId().toString());
            rs = ps.executeQuery();
            if (rs.next()){
                balance = rs.getDouble("Balance");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return balance;
    }

    public double getMoneyAmountRemaining(Connection connect){
        double balance = 0.0;
        try{
            ps = connect.prepareStatement(SQLSelectMoneyAmountRemaining);
            rs = ps.executeQuery();
            if (rs.next()){
                balance = rs.getDouble("MoneyAmount");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return balance;
    }

    public void updateBalance(Player player, int bal, String operation, Connection conn){
        try{
            double newBal;
            PreparedStatement ds = conn.prepareStatement(SQLSelectBalanceQuery);
            ds.setString(1,player.getUniqueId().toString());
            ts = ds.executeQuery();
            if (operation.equals("plus")) {
                ts.next();
                newBal = ts.getDouble("Balance") + bal;
                ps = conn.prepareStatement(SQLUpdateBalance);
                ps.setDouble(1, newBal);
                ps.setString(2, player.getUniqueId().toString());
                ps.executeUpdate();
            }else if (operation.equals("minus")){
                ts.next();
                newBal = ts.getDouble("Balance") - bal;
                if (newBal < 0){
                    player.sendMessage(ChatColor.translateAlternateColorCodes
                            ('&', "&2>&cInsufficient funds!"));
                }else {
                    ps = conn.prepareStatement(SQLUpdateBalance);
                    ps.setDouble(1, newBal);
                    ps.setString(2, player.getUniqueId().toString());
                    ps.executeUpdate();
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean increaseClaimblocks(Player player, int Amnt, Connection connect){
        boolean tri = false;
        try{
            int currentClaimblocks = getClaimblocks(player, connect);
            int playerId = this.getPlayerId(player.getUniqueId().toString(), connect);
            currentClaimblocks = currentClaimblocks + Amnt;
            ps = connect.prepareStatement(SQLUpdateClaimblocks);
            ps.setInt(1, currentClaimblocks);
            ps.setInt(2, playerId);
            rowsAffected = ps.executeUpdate();
            tri = rowsAffected > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return tri;
    }

    public void deleteClaimography(Player claimowner, Connection conn){
        try{
            int claimId = getClaimogrIdFromClaimography(claimowner.getUniqueId().toString(), conn);
            ps = conn.prepareStatement(SQLDeleteClaimography);
            ps.setInt(1, claimId);
            rowsAffected = ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public LocalDateTime getLastPlayerMovementTimestamp(Player player, Connection conn){
        LocalDateTime lastSeen = now();
        lastSeen = lastSeen.withHour(0);
        lastSeen = lastSeen.withMinute(1);
        try{;
            ps = conn.prepareStatement(SQLSelectPlayerTimestamp);
            ps.setString(1, player.getUniqueId().toString());
            rs = ps.executeQuery();
            if (rs.next()){
                String LastSeenDate = rs.getString("ActivityTimestamp");
                String[] strArray = LastSeenDate.split("/");
                lastSeen = lastSeen.withYear(Integer.parseInt(strArray[0]));
                lastSeen = lastSeen.withMonth(Integer.parseInt(strArray[1]));
                lastSeen = lastSeen.withDayOfMonth(Integer.parseInt(strArray[2]));
                lastSeen = lastSeen.withHour(Integer.parseInt(strArray[3]));
                lastSeen = lastSeen.withMinute(Integer.parseInt(strArray[4]));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return lastSeen;
    }

    public void increaseActivityIndicator(Player player, int Amnt, Connection conn){
        try{
            int currentActivity = getcurrentActivityIndicator(player, conn);
            int playerId = this.getPlayerId(player.getUniqueId().toString(), conn);
            currentActivity = currentActivity + Amnt;
            if (Amnt == 0){
                currentActivity = 0;
            }
            ps = conn.prepareStatement(SQLIncreaseActivityIndicator);
            ps.setInt(1, currentActivity);
            ps.setInt(2, playerId);
            rowsAffected = ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public int getcurrentActivityIndicator(Player player, Connection conn){
        try{
            ps = conn.prepareStatement(SQLSelectPlayerActivityIndicator);
            ps.setString(1, player.getUniqueId().toString());
            rs = ps.executeQuery();
            if (rs.next()){
                return rs.getInt(1);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public void updateLastPlayerMovmentTimestamp(Player player, LocalDateTime lastSeen, Connection conn){
        try{
            String TimestampInput = lastSeen.getYear()+"/"+lastSeen.getMonthValue()+"/"+lastSeen.getDayOfMonth()+"/"+
                    lastSeen.getHour()+"/"+lastSeen.getMinute();
            int claimId = this.getPlayerId(player.getUniqueId().toString(), conn);
            ps = conn.prepareStatement(SQLUpdateLastPlayerMovmentTimestamp);
            ps.setString(1, TimestampInput);
            ps.setInt(2, claimId);
            rowsAffected = ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public double getX1FromClaims(int ClaimId, Connection conn){
        double res = 0.0;
        try{
            ps = conn.prepareStatement(SQLSelectBlock1X);
            ps.setInt(1, ClaimId);
            rs = ps.executeQuery();
            if (rs.next()){
                res = rs.getDouble("Block1_X");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return res;
    }

    public double getX2FromClaims(int ClaimId, Connection conn){
        double res = 0.0;
        try{
            ps = conn.prepareStatement(SQLSelectBlock2X);
            ps.setInt(1, ClaimId);
            rs = ps.executeQuery();
            if (rs.next()){
                res = rs.getDouble("Block2_X");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return res;
    }

    public double getZ1FromClaims(int ClaimId, Connection conn){
        double res = 0.0;
        try{
            ps = conn.prepareStatement(SQLSelectBlock1Z);
            ps.setInt(1, ClaimId);
            rs = ps.executeQuery();
            if (rs.next()){
                res = rs.getDouble("Block1_Z");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return res;
    }

    public double getZ2FromClaims(int ClaimId, Connection conn){
        double res = 0.0;
        try{
            ps = conn.prepareStatement(SQLSelectBlock2Z);
            ps.setInt(1, ClaimId);
            rs = ps.executeQuery();
            if (rs.next()){
                res = rs.getDouble("Block2_Z");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return res;
    }

    public boolean deleteSpecificClaimById(int ClaimId, Connection conn){
        try{
            ps = conn.prepareStatement(SQLDeleteClaimById);
            ps.setInt(1, ClaimId);
            rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0){
                return true;
            }else{
                return false;
            }
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
