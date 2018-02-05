import cv2, sys

image = cv2.imread(sys.argv[1], cv2.IMREAD_UNCHANGED)
image_hdpi = cv2.resize(image, (72,72))
image_mdpi = cv2.resize(image, (48,48))
image_xhdpi = cv2.resize(image, (96,96))
image_xxhdpi = cv2.resize(image, (144,144))
image_xxxhdpi = cv2.resize(image, (192,192))

cv2.imwrite("mipmap-hdpi/upcc_logo.png", image_hdpi)
cv2.imwrite("mipmap-mdpi/upcc_logo.png", image_mdpi)
cv2.imwrite("mipmap-xhdpi/upcc_logo.png", image_xhdpi)
cv2.imwrite("mipmap-xxhdpi/upcc_logo.png", image_xxhdpi)
cv2.imwrite("mipmap-xxxhdpi/upcc_logo.png", image_xxxhdpi)