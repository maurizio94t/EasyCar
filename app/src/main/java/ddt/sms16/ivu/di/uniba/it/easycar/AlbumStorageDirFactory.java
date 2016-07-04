package ddt.sms16.ivu.di.uniba.it.easycar;

import java.io.File;

public abstract class AlbumStorageDirFactory {
    public abstract File getAlbumStorageDir(String albumName);
}
