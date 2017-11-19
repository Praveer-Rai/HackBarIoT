INSERT INTO `tododb`.`drink_item` (`id`, `drink_name`, `max_temp`, `min_temp`) VALUES ('1', 'Beer', '10', '0');
INSERT INTO `tododb`.`drink_item` (`id`, `drink_name`, `max_temp`, `min_temp`) VALUES ('2', 'Wine', '20', '10');
INSERT INTO `tododb`.`drink_item` (`id`, `drink_name`, `max_temp`, `min_temp`) VALUES ('3', 'Whiskey', '15', '5');
INSERT INTO `tododb`.`drink_item` (`id`, `drink_name`, `max_temp`, `min_temp`) VALUES ('4', 'Water', '40', '10');

UPDATE `tododb`.`drink_item` SET `imageurl`='https://emojipedia-us.s3.amazonaws.com/thumbs/120/emoji-one/104/beer-mug_1f37a.png', `max_temp`='30', `min_temp`='25', `price`='2' WHERE `id`='1';
UPDATE `tododb`.`drink_item` SET `imageurl`='https://emojipedia-us.s3.amazonaws.com/thumbs/120/emoji-one/104/wine-glass_1f377.png', `price`='3' WHERE `id`='2';
UPDATE `tododb`.`drink_item` SET `imageurl`='https://emojipedia-us.s3.amazonaws.com/thumbs/120/google/110/tumbler-glass_1f943.png', `price`='5' WHERE `id`='3';
UPDATE `tododb`.`drink_item` SET `imageurl`='https://emojipedia-us.s3.amazonaws.com/thumbs/120/facebook/111/glass-of-milk_1f95b.png', `price`='1' WHERE `id`='4';


INSERT INTO `tododb`.`user_item` (`id`, `current_drink`, `device_id`, `drink_count`, `find_my_drink`, `max_temp`, `min_temp`, `need_assistance`, `sip_count`, `user_id`) VALUES ('1', 'Beer', '1052512259', '0', false, '30', '25', false, '0', 'Nacho');
INSERT INTO `tododb`.`user_item` (`id`, `current_drink`, `device_id`, `drink_count`, `find_my_drink`, `max_temp`, `min_temp`, `need_assistance`, `sip_count`, `user_id`) VALUES ('2', 'Beer', '1052512258', '0', false, '30', '25', false, '0', 'Ben');
