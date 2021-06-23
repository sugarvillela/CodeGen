package tokenizer.util_iface;

/**Use to convert space delimiter to any-whitespace delimiter */
public interface IWhitespaceTest {
    default boolean eq(char symbol) {
        return false;
    }
}
