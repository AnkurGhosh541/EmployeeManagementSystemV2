package storage.user;

public abstract class BaseRepository {
    public UserStorageRepository repository;

    public abstract UserStorageRepository getRepository(String type);
}
