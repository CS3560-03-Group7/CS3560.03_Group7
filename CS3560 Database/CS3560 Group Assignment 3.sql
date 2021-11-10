	CREATE DATABASE CS3560F21;
    USE CS3560F21;
    
    INSERT INTO item(itemName, isAvailable,price) VALUES (1, "Burger", 0, 9.00);
    INSERT INTO item(itemName, isAvailable,price) VALUES ("Fries", 0, 2.50);
    INSERT INTO item(itemName, isAvailable,price) VALUES ("Shake", 0, 4.00);
    INSERT INTO item(itemName, isAvailable,price) VALUES ("Dino Nuggies", 0, 7.00);
    INSERT INTO item(itemName, isAvailable,price) VALUES ("Burger Combo", 0, 10.00);
    INSERT INTO item(itemName, isAvailable,price) VALUES ("Kids Meal", 0, 6.00);
    INSERT INTO item(itemName, isAvailable,price) VALUES ("Mega Burger", 0, 11.00);
    INSERT INTO item(itemName, isAvailable,price) VALUES ("Root Beer Float", 0, 6.50);
    
    UPDATE item SET price = 9.00 WHERE itemID = 1;
    UPDATE item SET price = 2.50 WHERE itemID = 2;
    
    SELECT * from item;