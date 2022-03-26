package com.star.app.game.helpers;

import java.util.ArrayList;
import java.util.List;

public abstract class ObjectPool<T extends Poolable> {
    // Список активных элементов
    protected List<T> activeList;
    // Список свободных элементов
    protected List<T> freeList;

    public List<T> getActiveList() {
        return activeList;
    }

    // Объясняем пулу объектов как и какой объект надо создавать
    protected abstract T newObject();

    // Освобождаем элемент, который отработал свое. Перекидываем его из списка
    // активных элементов в список свободных.
    public void free(int index) {
        freeList.add(activeList.remove(index));
    }

    public ObjectPool() {
        this.activeList = new ArrayList<T>();
        this.freeList = new ArrayList<T>();
    }

    // Запрашиваем элемент для работы. Если пул свободных элементов пуст, то в нем
    // создается новый объект. Получаем крайний элемент из свободных, убираем его из
    // списка свободных элементов, перекладываем в список активных и возвращаем
    // пользователю ссылку на только что полученный активный элемент чтобы его
    // можно было настроить
    public T getActiveElement() {
        if (freeList.size() == 0) {
            freeList.add(newObject());
        }
        T temp = freeList.remove(freeList.size() - 1);
        activeList.add(temp);
        return temp;
    }

    // Проверяем список активных элементов на наличие "отработавших свое". Если
    // такие находятся, то возвращаем их в список свободных элементов.
    public void checkPool() {
        for (int i = activeList.size() - 1; i >= 0; i--) {
            if (!activeList.get(i).isActive()) {
                free(i);
            }
        }
    }
}
