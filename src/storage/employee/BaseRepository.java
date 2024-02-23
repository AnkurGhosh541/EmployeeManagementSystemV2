package storage.employee;

public abstract class BaseRepository {
    public EmployeeStorageRepository repository;

    public abstract EmployeeStorageRepository getRepository(String type);
}
