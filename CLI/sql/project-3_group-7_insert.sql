INSERT INTO MANUFACTURER VALUES
    ('Sony',      '8881787537', '7 Pico Boulevard'),
    ('Microsoft', '8002961122', '98 Somerset Ave'),
    ('Meta',      '8003166455', '1900 Franklin Place'),
    ('Shell',     '8884241312', '2915 Berkshire'),
    ('Walmart',   '8005498513', '404 Mordor Lane'),
    ('Sansai',    '8006602121', '789 Lincoln Avenue'),
    ('Bonvita',   '8007309224', '56 Xavier Express Way'),
    ('Hyundai',   '8008924221', '789 Abbeywood Lane'),
    ('Pique',     '8189087410', '1567 Heaven Road'),
    ('Unreal',    '8000650121', '2345 Hibiscus Way');

INSERT INTO PRODUCT VALUES
    (1,  'Playstation 5', 'Sony'),
    (2,  'Bluray Player', 'Sony'),
    (3,  'USB-C Charger', 'Sony'),
    (4,  'AAA Battery', 'Sony'),
    (5,  '1TB HDD', 'Sony'),
    (6,  'Playstation 4', 'Sony'),
    (7,  'Playstation 3', 'Sony'),
    (8,  'Metal Gear Solid', 'Sony'),
    (9,  'Playstation Subscription', 'Sony'),
    (10, '4TB HDD', 'Sony'),
    
    (11, 'Word 2022', 'Microsoft'),
    (12, 'Xbox 360', 'Microsoft'),
    (13, 'Windows 12', 'Microsoft'),
    (14, 'Surface 7', 'Microsoft'),
    (15, 'Windows XP', 'Microsoft'),
    (16, 'Xbox Controller S', 'Microsoft'),
    
    (17, 'Oculus VR', 'Meta'),
    (18, 'Blaze Phone', 'Meta'),
    (19, '1-Month Subscription', 'Meta'),
    
    (20, 'VR Headset', 'Walmart'),
    (21, 'Xbox 4', 'Walmart'),
    (22, 'UHDTV 72', 'Walmart'),
    (23, 'UHDTV 77', 'Walmart'),
    (24, 'Irish Spring Soap', 'Walmart'),
    
    (25, 'Synthetic Oil', 'Shell'),
    
    (26, 'iPhone 11', 'Sansai'),
    (27, 'iPhone 12', 'Sansai'),
    (28, 'Andoid S8', 'Sansai'),
    (29, 'Android S12', 'Sansai'),
    (30, 'Android 2022', 'Sansai'),
    (31, 'Pixel 6', 'Sansai'),
    (32, 'Pixel 10', 'Sansai'),
    
    (33, 'Klondite Bar', 'Bonvita'),
    (34, 'Jello', 'Bonvita'),
    (35, 'Minute Maid', 'Bonvita'),
    (36, 'Apple Cider', 'Bonvita'),
    (37, 'Root Beer', 'Bonvita'),
    
    (38, 'Selantra 2021', 'Hyundai'),
    (39, 'Elantra 2006', 'Hyundai'),
    
    (40, 'Neon Headband Medium', 'Pique'),
    (41, 'Knee Pads X', 'Pique'),
    (42, 'Inline Skate Pro 6', 'Pique'),
    (43, 'Ultra Helmet 2021', 'Pique'),
    (44, 'Quad Skate Pro 7', 'Pique'),
    
    (45, 'Infinity Tournament 2022', 'Unreal'),
    (46, 'Infinity Tournament 2012', 'Unreal');
    
INSERT INTO STORE VALUES
    (1, 'Divine Purchases', '767 Malcolm Boulevard', 3238995057),
    (2, 'Best Deals', '12 Inverness Highway', 5623720926),
    (3, 'Cost Less', '420 Chilltown Avenue', 3109122357);

INSERT INTO BELONGS_TO VALUES
    (1, 1, 489.99, 499.99, 77),
    (1, 11, 899.99, 999.99, 1124),
    (1, 32, 1499.99, 1499.99, 48),
    (1, 24, 9.99, 12.99, 952),
    (1, 36, 7.99, 8.99, 1731),
    (1, 44, 489.99, 499.99, 100),
    (1, 19, 58.99, 59.99, 99994),
    (1, 33, 489.99, 499.99, 100),
    (1, 16, 99.99, 119.99, 159),
    (1, 23, 479.99, 599.99, 10001),
    
    (2, 1, 489.99, 499.99, 77),
    (2, 2, 899.99, 999.99, 1124),
    (2, 3, 1499.99, 1499.99, 48),
    (2, 4, 9.99, 12.99, 952),
    (2, 5, 7.99, 8.99, 1731),
    (2, 6, 489.99, 499.99, 100),
    (2, 7, 58.99, 59.99, 99994),
    (2, 8, 489.99, 499.99, 100),
    (2, 9, 99.99, 119.99, 159),
    (2, 23, 479.99, 599.99, 10001),
    
    (3, 45, 489.99, 499.99, 77),
    (3, 1, 899.99, 999.99, 1124),
    (3, 11, 1499.99, 1499.99, 48),
    (3, 12, 9.99, 12.99, 952),
    (3, 44, 7.99, 8.99, 1731),
    (3, 19, 489.99, 499.99, 100),
    (3, 20, 58.99, 59.99, 99994),
    (3, 10, 489.99, 499.99, 100),
    (3, 21, 99.99, 119.99, 159),
    (3, 23, 479.99, 599.99, 10001);

INSERT INTO CUSTOMER VALUES
    (1,  '4246141111', 'Jackie', 'Chan'),
    (2,  '4241542120', 'Michael', 'Jordan'),
    (3,  '4240645132', 'Thomas', 'Mapother'),
    (4,  '4247311214', 'Sean', 'Connery'),
    (5,  '4244007751', 'Curtis', 'Jackson'),
    (6,  '4243017724', 'Barack', 'Obama'),
    (7,  '4248140145', 'Oprah', 'Winfrey'),
    (8,  '4241548451', 'Viola', 'Davis'),
    (9,  '4240075148', 'Angela', 'Bassett'),
    (10, '4247741245', 'Omotola', 'Ekeinde');

INSERT INTO SALES VALUES
    ('P000012345', 1, '2020-01-01 18:44:14'),
    ('P000012346', 1, '2021-01-01 18:34:44'),
    ('P000012347', 2, '2022-01-01 18:04:44'),
    ('P000312344', 3, '2006-01-01 18:34:44'),
    ('P000002349', 3, '2011-01-01 18:14:44'),
    ('P000012344', 1, '2021-01-01 18:44:44');
    
INSERT INTO CUST_SALES VALUES
    ('P000012345', 1);
    
INSERT INTO SALES_ITEM VALUES
    ('P000012345', 1, 2),
    ('P000012346', 1, 1),
    ('P000012347', 2, 3),
    ('P000312344', 3, 1),
    ('P000002349', 3, 1),
    ('P000012344', 1, 1);