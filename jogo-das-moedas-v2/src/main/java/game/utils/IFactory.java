package game.utils;

public interface IFactory<E, K> {

    E create(K key);

}