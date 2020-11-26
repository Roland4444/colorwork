DELETE FROM properties where
     name like 'scale%' or
     name like 'endpoint.camera%' or
     name = 'camera.height' or
     name = 'camera.width'