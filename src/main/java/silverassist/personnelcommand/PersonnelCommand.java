package silverassist.personnelcommand;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import silverassist.personnelcommand.command.Hane;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public final class PersonnelCommand extends JavaPlugin {

    public static Firestore db = null;
    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();
        InputStream serviceAccount = null;
        try {
            serviceAccount = new FileInputStream((String) this.getConfig().get("path"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        GoogleCredentials credentials = null;
        try {
            credentials = GoogleCredentials.fromStream(serviceAccount);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .build();
        FirebaseApp.initializeApp(options);


        db = FirestoreClient.getFirestore();
        //レポートコマンド
        PluginCommand command = getCommand("hane");
        if(command != null)command.setExecutor(new Hane());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }

}
