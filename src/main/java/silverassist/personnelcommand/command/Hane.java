package silverassist.personnelcommand.command;

import com.google.cloud.firestore.*;
import com.google.firebase.database.annotations.NotNull;
import org.bukkit.BanList;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.bukkit.Bukkit.*;
import static silverassist.personnelcommand.PersonnelCommand.db;


public class Hane implements CommandExecutor {
    private final String prefix ="§b§l[§e§lPersonnelCommand§b§l]§r";
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length != 3)return false;
        OfflinePlayer p = getOfflinePlayer(args[1]);
        if(p == null) return false;

        CollectionReference doc = db.collection("punisherlist");
        DocumentSnapshot CntDoc = null;
        try {
            CntDoc = doc.document("cnt").get().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if(CntDoc == null)return true;
        String cnt = null;
        Map<String, Object> Data = new HashMap<>();


        switch (args[0]) {
            case "ban":
                if (!CntDoc.exists()) SetCnt(doc.document("cnt"), 0); //総数記憶用ガ無ければ製作
                int c = Math.toIntExact(CntDoc.getLong("cnt")); //総数を取得
                SetCnt(doc.document("cnt"), c+1); //総数を1追加
                cnt = String.valueOf(c);

                getBanList(BanList.Type.NAME).addBan(args[1], args[2], null, sender.getName()); //banリストに追加
                Data.put("cond", true);
                Data.put("date", new Date());
                Data.put("reason", args[2]);
                Data.put("runner", sender.getName());
                Data.put("term", "無期限");
                Data.put("uuid", String.valueOf(p.getUniqueId()));
                Data.put("type", "ban");
                doc.document(cnt).set(Data); //dbに送信

                //プレイヤーキック
                Player p2 = getPlayer(args[1]);
                if(p2 !=null)p2.kickPlayer("このサーバへのアクセスが禁止されました");
                return true;

            case "pardon":
                try {
                    //一番最後のban履歴を取得
                    cnt = SearchByUUID(doc,p.getUniqueId(),"ban");
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                //存在確認
                if(cnt==null){
                    sender.sendMessage(this.prefix+"§c§lそのプレイヤーは現在執行中のBANが存在しません");
                    return true;
                }

                getBanList(BanList.Type.NAME).pardon(p.getName()); //banリストから削除
                Data = new HashMap<>();
                Data.put("cond", false);
                Data.put("reason",args[2]);
                doc.document(cnt).set(Data, SetOptions.merge()); //dbに送信
                return true;
        }
        return false;
    }

    //総数変更
    private void SetCnt(DocumentReference doc, int cnt){
        Map<String,Object> docData = new HashMap<>();
        docData.put("cnt",cnt);
        doc.set(docData);
    }

    //uuidとtypeから最後尾を捜索
    private String SearchByUUID(CollectionReference doc, UUID uuid, String type) throws ExecutionException, InterruptedException {
        QuerySnapshot k = doc.whereEqualTo("uuid", String.valueOf(uuid)).whereEqualTo("cond",true).whereEqualTo("type",type).get().get();
        if(k==null) return null;
        if(k.getDocuments().isEmpty())return null;
        return k.getDocuments().get(k.getDocuments().size()-1).getId(); //最後尾を返す
    }


}
